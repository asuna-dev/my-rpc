package org.zepe.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.registry.RegistryFactory;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(serviceName).methodName(method.getName())
            .parameterTypes(method.getParameterTypes()).args(args).build();

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        //        serviceMetaInfo.setServiceVersion();
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            throw new RuntimeException("service unavailable: " + serviceName);
        }

        //todo
        ServiceMetaInfo svcMetaInfo = serviceMetaInfos.get(0);

        try (HttpResponse httpResponse = HttpRequest.post(svcMetaInfo.getServiceAddress())
            .body(serializer.serialize(rpcRequest)).execute()) {
            byte[] result = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            log.info("RpcResponse: {}", rpcResponse);
            return rpcResponse.getData();

        } catch (Exception e) {
            throw new RuntimeException("rpc service error", e);
        }

    }
}
