package org.zepe.rpc.server;

import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.RpcStatusCode;
import org.zepe.rpc.registry.LocalRegistry;

import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/26 21:53
 * @description
 */
public class RpcRequestHandler {
    static public RpcResponse handleRpcRequest(RpcRequest request) {
        RpcResponse response;

        try {
            Class<?> service = LocalRegistry.getService(request.getServiceName());
            Method method = service.getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(service.getDeclaredConstructor().newInstance(), request.getArgs());
            response = RpcResponse.success(result, method.getReturnType());

        } catch (Exception e) {
            response = RpcResponse.failure(RpcStatusCode.INTERNAL_SERVER_ERROR, e);
        }

        return response;
    }
}
