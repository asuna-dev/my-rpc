package org.zepe.rpc.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.example.model.RpcRequest;
import org.zepe.rpc.example.model.RpcResponse;
import org.zepe.rpc.example.serializer.Serializer;
import org.zepe.rpc.example.serializer.impl.JdkSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/23 22:24
 * @description
 */
public class ServiceProxy implements InvocationHandler {
    private static final Log log = LogFactory.get();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest =
            RpcRequest.builder().serviceName(method.getDeclaringClass().getName()).methodName(method.getName())
                .parameterTypes(method.getParameterTypes()).args(args).build();
        try {
            byte[] body = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:9999/rpc").body(body).execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        } catch (Exception e) {
            log.error(e);
        }

        return null;
    }
}
