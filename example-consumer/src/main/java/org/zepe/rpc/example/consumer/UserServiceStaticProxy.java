package org.zepe.rpc.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.impl.JdkSerializer;

import java.io.IOException;

/**
 * @author zzpus
 * @datetime 2025/4/23 22:24
 * @description
 */
public class UserServiceStaticProxy implements UserService {
    private static final Log log = LogFactory.get();

    @Override
    public User getUserByName(String name) {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest =
            RpcRequest.builder().serviceName(UserService.class.getName()).methodName("getUserByName")
                .args(new Object[] {name}).parameterTypes(new Class[] {name.getClass()}).build();

        try {
            byte[] body = serializer.serialize(rpcRequest);
            byte[] result;
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            String url = "http://" + rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort() + "/rpc";
            try (HttpResponse httpResponse = HttpRequest.post(url).body(body).execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User)rpcResponse.getData();
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }
}
