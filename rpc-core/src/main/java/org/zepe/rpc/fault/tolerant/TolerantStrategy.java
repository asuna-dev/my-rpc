package org.zepe.rpc.fault.tolerant;

import org.zepe.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:05
 * @description
 */
public interface TolerantStrategy {
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
