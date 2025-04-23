package org.zepe.rpc.example.registry;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:39
 * @description
 */
public class LocalRegistry {
    //    private static final ;

    private static final Log log = LogFactory.get();

    private static final Map<String, Class<?>> registry = new ConcurrentHashMap<>();

    public static void register(String serviceName, Class<?> implClass) {
        log.info("register service: {} -> {}", serviceName, implClass.getName());
        registry.put(serviceName, implClass);
    }

    public static Class<?> getService(String serviceName) {
        return registry.get(serviceName);
    }

    public static void unregister(String serviceName) {
        log.info("unregister service: {}", serviceName);
        registry.remove(serviceName);
    }
}
