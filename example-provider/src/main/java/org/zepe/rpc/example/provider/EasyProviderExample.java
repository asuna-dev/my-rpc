package org.zepe.rpc.example.provider;

import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.example.common.service.ComputeService;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.*;
import org.zepe.rpc.server.HttpServer;
import org.zepe.rpc.server.tcp.VertxTcpServer;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:53
 * @description
 */
public class EasyProviderExample {
    public static void main(String[] args) throws Exception {
        Registry registry = RegistryFactory.getInstance(RegistryKeys.ETCD);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        ServiceMetaInfo userServiceInfo = new ServiceMetaInfo();
        userServiceInfo.setServiceHost(rpcConfig.getServerHost());
        userServiceInfo.setServicePort(rpcConfig.getServerPort());
        userServiceInfo.setServiceName(UserService.class.getName());

        registry.register(userServiceInfo);

        ServiceMetaInfo computeServiceInfo = new ServiceMetaInfo();
        computeServiceInfo.setServiceHost(rpcConfig.getServerHost());
        computeServiceInfo.setServicePort(rpcConfig.getServerPort());
        computeServiceInfo.setServiceName(ComputeService.class.getName());

        registry.register(computeServiceInfo);

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        LocalRegistry.register(ComputeService.class.getName(), ComputeServiceImpl.class);

        HttpServer httpServer = new VertxTcpServer();
        httpServer.doStart(rpcConfig.getServerPort());

    }
}
