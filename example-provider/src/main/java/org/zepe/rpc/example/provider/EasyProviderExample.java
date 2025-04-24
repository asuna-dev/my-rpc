package org.zepe.rpc.example.provider;

import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.registry.LocalRegistry;
import org.zepe.rpc.server.HttpServer;
import org.zepe.rpc.server.VertxHttpServer;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:53
 * @description
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
