package org.zepe.rpc.fault.tolerant;

import org.zepe.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:05
 * @description
 */
public interface TolerantStrategyKeys {
    String FAIL_BACK = "failBack";
    String FAIL_FAST = "failFast";
    String FAIL_OVER = "failOver";
    String FAIL_SAFE = "failSafe";
}
