package org.zepe.rpc.config;

import lombok.Data;
import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.fault.retry.RetryStrategyKeys;
import org.zepe.rpc.fault.tolerant.TolerantStrategyKeys;
import org.zepe.rpc.loadbalancer.LoadBalancerKeys;
import org.zepe.rpc.serializer.SerializerKeys;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:14
 * @description
 */
@Data
public class RpcConfig implements Serializable {
    boolean mock = false;
    // rpc框架序列化协议
    private String serializer = SerializerKeys.JDK;
    private String name = "default";
    // rpc框架版本
    private String version = "0.0.1";
    // 机器服务分组(可用于实现泳道？)
    private String group = RpcConstant.DEFAULT_SERVICE_GROUP;
    // 服务器ip
    private String serverHost = "127.0.0.1";
    private Integer serverPort = 9876;
    // 消费端负载均衡策略
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    // 消费端重试策略
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;
    // 消费端容错策略
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    private RegistryConfig registryConfig = new RegistryConfig();
}
