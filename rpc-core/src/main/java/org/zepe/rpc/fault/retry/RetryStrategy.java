package org.zepe.rpc.fault.retry;

import org.zepe.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author zzpus
 * @datetime 2025/4/26 23:07
 * @description
 */
public interface RetryStrategy {
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
