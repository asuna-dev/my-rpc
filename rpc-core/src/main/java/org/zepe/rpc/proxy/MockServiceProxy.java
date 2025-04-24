package org.zepe.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/24 12:48
 * @description
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("mock invoke {}", method.getName());
        Class<?> returnType = method.getReturnType();
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (boolean.class == clazz) {
                return false;
            }
            if (byte.class == clazz) {
                return (byte)0;
            }
            if (char.class == clazz) {
                return (char)0;
            }
            if (short.class == clazz) {
                return (short)0;
            }
            if (int.class == clazz) {
                return 0;
            }
            if (long.class == clazz) {
                return 0L;
            }
            if (float.class == clazz) {
                return (float)0.0;
            }
            if (double.class == clazz) {
                return 0.0;
            }
        }

        return null;
    }
}
