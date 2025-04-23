package org.zepe.rpc.example.consumer;

import java.lang.reflect.Proxy;

/**
 * @author zzpus
 * @datetime 2025/4/23 23:38
 * @description
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T)Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, new ServiceProxy());
    }
}
