package org.zepe.rpc.fault.retry.impl;

import cn.hutool.core.util.StrUtil;
import com.github.rholder.retry.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.fault.retry.RetryStrategy;
import org.zepe.rpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author zzpus
 * @datetime 2025/4/26 23:16
 * @description 默认2s重试一次，重试三次
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class FixedIntervalRetryStrategy implements RetryStrategy {
    private int count = 2;
    private long time = 3;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
            .retryIfException()
            .withWaitStrategy(WaitStrategies.fixedWait(time, timeUnit))
            .withStopStrategy(StopStrategies.stopAfterAttempt(count))
            .withRetryListener(new RetryListener() {
                @Override
                public <V> void onRetry(Attempt<V> attempt) {
                    String msg;
                    if (attempt.hasException()) {
                        msg = StrUtil.format("retry count: {}, cause: {}", attempt.getAttemptNumber(),
                            attempt.getExceptionCause().getMessage());
                    } else {
                        msg = StrUtil.format("retry count: {}", attempt.getAttemptNumber());
                    }
                    log.info("{}", msg);
                }
            })
            .build();
        return retryer.call(callable);
    }
}
