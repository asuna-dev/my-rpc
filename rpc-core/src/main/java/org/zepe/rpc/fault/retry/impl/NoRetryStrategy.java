package org.zepe.rpc.fault.retry.impl;

import org.zepe.rpc.fault.retry.RetryStrategy;
import org.zepe.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author zzpus
 * @datetime 2025/4/26 23:13
 * @description
 */
public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
