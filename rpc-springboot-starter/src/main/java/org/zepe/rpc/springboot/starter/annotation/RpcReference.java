package org.zepe.rpc.springboot.starter.annotation;

import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.fault.retry.RetryStrategyKeys;
import org.zepe.rpc.fault.tolerant.TolerantStrategyKeys;
import org.zepe.rpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zzpus
 * @datetime 2025/4/27 14:18
 * @description 服务消费者注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {
    Class<?> interfaceClass() default void.class;

    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    String retryStrategy() default RetryStrategyKeys.NO;

    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;
}
