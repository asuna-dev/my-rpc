package org.zepe.rpc.example.provider;

import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.registry.*;
import org.zepe.rpc.server.HttpServer;
import org.zepe.rpc.server.VertxHttpServer;
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

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceName(UserService.class.getName());

        registry.register(serviceMetaInfo);

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer httpServer = new VertxTcpServer();
        httpServer.doStart(rpcConfig.getServerPort());

    }
}
