package org.zepe.rpc.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.constant.RpcConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:22
 * @description
 */
@Slf4j
class ConfigUtilsTest {

    @Test
    void loadConfig() {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX, "test");
        log.info("{}", rpcConfig);
    }
}