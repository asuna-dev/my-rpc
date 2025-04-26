package org.zepe.rpc.fault.retry.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.zepe.rpc.model.RpcResponse;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author zzpus
 * @datetime 2025/4/26 23:25
 * @description
 */
@Slf4j
public class FixedIntervalRetryStrategyTest {
    int count = 0;

    @Test
    public void test() throws Exception {

        RpcResponse rpcResponse = new FixedIntervalRetryStrategy(5, 2, TimeUnit.SECONDS).doRetry(() -> {
            count++;
            log.info("call count: {}", count);
            if (count < 3) {
                throw new RuntimeException("mock exception");
            } else {
                return new RpcResponse();
            }
        });

        Assert.assertEquals(3, count);
        Assert.assertNotNull(rpcResponse);
    }
}