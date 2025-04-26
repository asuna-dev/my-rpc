package org.zepe.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.fault.retry.RetryStrategy;
import org.zepe.rpc.fault.retry.RetryStrategyFactory;
import org.zepe.rpc.fault.tolerant.TolerantStrategy;
import org.zepe.rpc.fault.tolerant.TolerantStrategyFactory;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.loadbalancer.LoadBalancerFactory;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.registry.RegistryFactory;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;
import org.zepe.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // final Serializer serializer = SerializerFactory.getSerializer(rpcConfig.getSerializer());

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
            .serviceName(serviceName)
            .methodName(method.getName())
            .parameterTypes(method.getParameterTypes())
            .args(args)
            .build();

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            throw new RuntimeException("service unavailable: " + serviceName);
        }

        final LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestArgs = new HashMap<>();
        requestArgs.put("methodName", rpcRequest.getMethodName());

        RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
        final ServiceMetaInfo svc = loadBalancer.select(requestArgs, serviceMetaInfos);
        RpcResponse rpcResponse;
        try {
            rpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(rpcRequest, svc));
            return rpcResponse.getData();

        } catch (Exception e) {
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }
}