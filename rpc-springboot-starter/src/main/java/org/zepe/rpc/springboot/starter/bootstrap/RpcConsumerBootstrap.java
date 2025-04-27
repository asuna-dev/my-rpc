package org.zepe.rpc.springboot.starter.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.zepe.rpc.fault.retry.RetryStrategyFactory;
import org.zepe.rpc.fault.tolerant.TolerantStrategyFactory;
import org.zepe.rpc.loadbalancer.LoadBalancerFactory;
import org.zepe.rpc.proxy.ServiceProxyFactory;
import org.zepe.rpc.springboot.starter.annotation.RpcReference;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/27 14:34
 * @description
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();

        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (void.class == interfaceClass) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);

                Map<String, Object> properties = new HashMap<>();
                properties.put("serviceVersion", rpcReference.serviceVersion());
                properties.put("loadBalancer", LoadBalancerFactory.getInstance(rpcReference.loadBalancer()));
                properties.put("retryStrategy", RetryStrategyFactory.getInstance(rpcReference.retryStrategy()));
                properties.put("tolerantStrategy",
                    TolerantStrategyFactory.getInstance(rpcReference.tolerantStrategy()));

                Object proxy = ServiceProxyFactory.getProxy(interfaceClass, properties);
                // 注入代理对象
                try {
                    field.set(bean, proxy);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("注入代理对象失败", e);
                }

            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
