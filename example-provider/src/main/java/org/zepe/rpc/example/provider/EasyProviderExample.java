package org.zepe.rpc.example.provider;

import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.bootstrap.ProviderBootstrap;
import org.zepe.rpc.constant.RpcConstant;
import org.zepe.rpc.example.common.service.ComputeService;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.example.provider.service.ComputeServiceImpl;
import org.zepe.rpc.example.provider.service.UserServiceImpl;
import org.zepe.rpc.model.ServiceLocalRegisterInfo;
import org.zepe.rpc.server.HttpServer;
import org.zepe.rpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:53
 * @description
 */
public class EasyProviderExample {
    public static void main(String[] args) throws Exception {
        List<ServiceLocalRegisterInfo<?>> svcs = new ArrayList<>();
        svcs.add(new ServiceLocalRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class,
            RpcConstant.DEFAULT_SERVICE_VERSION));
        svcs.add(new ServiceLocalRegisterInfo<>(ComputeService.class.getName(), ComputeServiceImpl.class,
            RpcConstant.DEFAULT_SERVICE_VERSION));

        ProviderBootstrap.init(svcs);

        HttpServer httpServer = new VertxTcpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
