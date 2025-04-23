package org.zepe.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:17
 * @description
 */
public class ConfigUtils {
    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    public static <T> T loadConfig(Class<T> clazz, String prefix, String env) {
        StringBuilder configFileStb = new StringBuilder("application");
        if (StrUtil.isNotBlank(env)) {
            configFileStb.append("-").append(env);
        }
        configFileStb.append(".properties");
        Props props = new Props(configFileStb.toString());
        return props.toBean(clazz, prefix);
    }
}
