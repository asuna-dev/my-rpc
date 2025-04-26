package org.zepe.rpc.spi;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.serializer.Serializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzpus
 * @datetime 2025/4/24 15:37
 * @description
 */
@Slf4j
public class SpiLoader {
    // 已加载的类：接口名=》（key=》实现类）
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();
    // 对象实例缓存 类路径=》对象实例
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    private static final String[] SCAN_DIRS = new String[] {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    private static final List<Class<?>> LOAD_CLASS_LIST =
        ListUtil.toList(Serializer.class, Registry.class, LoadBalancer.class);

    public static void loadAll() {
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("spi load class: {}", loadClass.getName());

        Map<String, Class<?>> classMap = new HashMap<>();

        for (String scanDir : SCAN_DIRS) {
            log.info("spi scan dir: {}", scanDir);
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            for (URL resource : resources) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strings = line.split("=");
                        if (strings.length > 1) {
                            String key = strings[0].toLowerCase();
                            String className = strings[1];
                            classMap.put(key, Class.forName(className));
                            log.info("spi loaded: {}: {}", key, className);
                        } else {
                            log.warn("invalid spi info: {}", line);
                        }
                    }
                } catch (Exception e) {
                    log.error("spi load error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), classMap);

        return classMap;
    }

    public static <T> T getInstance(Class<?> clazz, String key) {
        key = key.toLowerCase();
        String clazzName = clazz.getName();
        Map<String, Class<?>> classMap = loaderMap.get(clazzName);
        if (classMap == null) {
            throw new RuntimeException(StrUtil.format("unloaded class: {} ", clazzName));
        }

        Class<?> implClass = classMap.get(key);
        if (implClass == null) {
            throw new RuntimeException(StrUtil.format("unloaded class: {} ,key: {}", clazzName, key));
        }

        String implClassName = implClass.getName();

        Object object = instanceCache.get(implClassName);
        if (object == null) {
            try {
                object = implClass.getDeclaredConstructor().newInstance();
                instanceCache.put(implClassName, object);
                log.info("load instance of {}", implClassName);
            } catch (Exception e) {
                throw new RuntimeException(StrUtil.format("get instance of {} failed", implClassName), e);
            }
        }
        return (T)object;
    }

}
