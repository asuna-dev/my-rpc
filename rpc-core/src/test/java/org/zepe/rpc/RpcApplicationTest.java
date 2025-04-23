package org.zepe.rpc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.zepe.rpc.config.RpcConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:42
 * @description
 */
@Slf4j
public class RpcApplicationTest {

    @Test
    public void testGetConfig() {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        log.info("{}", rpcConfig);
    }
}