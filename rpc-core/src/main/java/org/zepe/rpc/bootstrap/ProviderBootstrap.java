package org.zepe.rpc.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.model.ServiceLocalRegisterInfo;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.LocalRegistry;
import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.registry.RegistryFactory;

import java.util.List;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:49
 * @description
 */
@Slf4j
public class ProviderBootstrap {
    public static void init(List<ServiceLocalRegisterInfo<?>> serviceLocalRegisterInfos) {
        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceLocalRegisterInfo<?> svc : serviceLocalRegisterInfos) {
            // 本地注册
            LocalRegistry.register(svc.getServiceName(), svc.getImplClass());
            // 服务中心注册
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                .serviceName(svc.getServiceName())
                .serviceVersion(svc.getServiceVersion())
                .serviceGroup(rpcConfig.getGroup())
                .serviceHost(rpcConfig.getServerHost())
                .servicePort(rpcConfig.getServerPort())
                .build();
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(svc.getServiceName() + "注册失败", e);
            }
        }
        // 启动服务器
        // new VertxTcpServer().doStart(rpcConfig.getServerPort());
    }
}
