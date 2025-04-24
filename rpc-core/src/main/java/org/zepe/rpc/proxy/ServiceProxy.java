package org.zepe.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/23 22:24
 * @description
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoke: {}", method);
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        final Serializer serializer = SerializerFactory.getSerializer(rpcConfig.getSerializer());

        RpcRequest rpcRequest =
            RpcRequest.builder().serviceName(method.getDeclaringClass().getName()).methodName(method.getName())
                .parameterTypes(method.getParameterTypes()).args(args).build();
        byte[] body = serializer.serialize(rpcRequest);
        byte[] result;
        String url = "http://" + rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort() + "/rpc";
        try (HttpResponse httpResponse = HttpRequest.post(url).body(body).execute()) {
            result = httpResponse.bodyBytes();
        }
        RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
        log.info("RpcResponse: {}", rpcResponse);
        return rpcResponse.getData();
    }
}
