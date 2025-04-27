package org.zepe.rpc.springboot.starter.annotation;

import org.springframework.context.annotation.Import;
import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import org.zepe.rpc.springboot.starter.bootstrap.RpcInitBootstrap;
import org.zepe.rpc.springboot.starter.bootstrap.RpcProviderBootstrap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zzpus
 * @datetime 2025/4/27 14:18
 * @description 启用自定义rpc注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {
    /**
     * 是否启动server
     */
    boolean needServer() default true;

    /**
     * 服务分组
     */
    // String serviceGroup() default RpcConstant.DEFAULT_SERVICE_GROUP;
}
