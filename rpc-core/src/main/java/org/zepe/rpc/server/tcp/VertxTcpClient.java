package org.zepe.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.ServiceMetaInfo;
import org.zepe.rpc.protocol.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author zzpus
 * @datetime 2025/4/25 23:32
 * @description
 */
@Slf4j
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception {
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        client.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {

            if (result.succeeded()) {
                NetSocket socket = result.result();
                ProtocolMessage<RpcRequest> request = new ProtocolMessage<>();
                ProtocolMessage.Header header = new ProtocolMessage.Header();
                header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                header.setSerializer(
                    (byte)ProtocolMessageSerializerEnum.getEnumByKey(RpcApplication.getRpcConfig().getSerializer())
                        .getValue());
                header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
                header.setRequestId(IdUtil.getSnowflakeNextId());
                request.setHeader(header);
                request.setBody(rpcRequest);

                try {
                    Buffer encode = ProtocolMessageEncoder.encode(request);
                    socket.write(encode);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                socket.handler(buffer -> {
                    try {
                        ProtocolMessage<RpcResponse> response =
                            (ProtocolMessage<RpcResponse>)ProtocolMessageDecoder.decode(buffer);
                        responseFuture.complete(response.getBody());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            } else {
                responseFuture.completeExceptionally(new RuntimeException("server error", result.cause()));
            }

        });

        RpcResponse response = responseFuture.get();
        client.close();
        return response;
    }
}
