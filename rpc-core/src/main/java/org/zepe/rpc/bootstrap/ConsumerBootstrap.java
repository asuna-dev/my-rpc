package org.zepe.rpc.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:49
 * @description
 */
@Slf4j
public class ConsumerBootstrap {
    public static void init() {
        RpcApplication.init();
    }
}
