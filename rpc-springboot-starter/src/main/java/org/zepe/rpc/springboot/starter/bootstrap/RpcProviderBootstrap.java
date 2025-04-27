package org.zepe.rpc.springboot.starter.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.LocalRegistry;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.registry.RegistryFactory;
import org.zepe.rpc.springboot.starter.annotation.RpcService;

/**
 * @author zzpus
 * @datetime 2025/4/27 14:34
 * @description
 */
public class RpcProviderBootstrap implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            Class<?> interfaceClass = rpcService.interfaceClass();
            if (void.class == interfaceClass) {
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String svcName = interfaceClass.getName();
            String svcVersion = rpcService.serviceVersion();

            LocalRegistry.register(svcName, beanClass);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                .serviceName(svcName)
                .serviceVersion(svcVersion)
                .serviceGroup(rpcConfig.getGroup())
                .serviceHost(rpcConfig.getServerHost())
                .servicePort(rpcConfig.getServerPort())
                .build();

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceMetaInfo.getServiceNodeKey() + "服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
