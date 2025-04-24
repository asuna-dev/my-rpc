package org.zepe.rpc.proxy;

import org.zepe.rpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @author zzpus
 * @datetime 2025/4/23 23:38
 * @description
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T)Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, new ServiceProxy());
    }

    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T)Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass},
            new MockServiceProxy());
    }
}
