package org.zepe.rpc.fault.tolerant.impl;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.fault.tolerant.TolerantStrategy;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.RpcStatusCode;

import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:08
 * @description
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 实现降级服务调用
        return RpcResponse.failure(RpcStatusCode.INTERNAL_SERVER_ERROR, e);
    }
}
