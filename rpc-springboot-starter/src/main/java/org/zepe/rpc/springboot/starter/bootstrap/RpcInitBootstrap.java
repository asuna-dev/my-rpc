package org.zepe.rpc.springboot.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.server.tcp.VertxTcpServer;
import org.zepe.rpc.springboot.starter.annotation.EnableRpc;

/**
 * @author zzpus
 * @datetime 2025/4/27 14:34
 * @description spring初始化时初始化rpc框架
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean needServer =
            (boolean)importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        RpcApplication.init();
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动rpc提供服务");
        }

        // ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
