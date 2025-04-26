package org.zepe.rpc.fault.tolerant.impl;

import org.zepe.rpc.fault.tolerant.TolerantStrategy;
import org.zepe.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:08
 * @description
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("service error", e);
    }
}
