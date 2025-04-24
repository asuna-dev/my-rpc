package org.zepe.rpc;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.registry.RegistryFactory;
import org.zepe.rpc.utils.ConfigUtils;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:35
 * @description
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newConfig) {
        log.info("rpc init config: {}", newConfig);

        rpcConfig = newConfig;
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
    }

    public static void init() {
        RpcConfig newConfig;
        try {
            newConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            log.error("rpc init error", e);
            newConfig = new RpcConfig();
        }
        init(newConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
