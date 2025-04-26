package org.zepe.rpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.RpcStatusCode;
import org.zepe.rpc.protocol.ProtocolMessage;
import org.zepe.rpc.protocol.ProtocolMessageDecoder;
import org.zepe.rpc.protocol.ProtocolMessageEncoder;
import org.zepe.rpc.protocol.ProtocolMessageTypeEnum;
import org.zepe.rpc.registry.LocalRegistry;

import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/26 13:24
 * @description
 */
@Slf4j
public class TcpServerHandler implements Handler<NetSocket> {

    private RpcResponse handleRpcRequest(RpcRequest request) {
        RpcResponse response;

        try {
            Class<?> service = LocalRegistry.getService(request.getServiceName());
            Method method = service.getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(service.getDeclaredConstructor().newInstance(), request.getArgs());
            response = RpcResponse.success(result, method.getReturnType());

        } catch (Exception e) {
            response = RpcResponse.failure(RpcStatusCode.INTERNAL_SERVER_ERROR, e);
        }

        return response;
    }

    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // TcpBufferHandlerWrapper此时已经传入了组装完整的ProtocolMessage请求
            log.info("receive request from: {}", netSocket.remoteAddress());

            ProtocolMessage<RpcRequest> message;
            try {
                message = (ProtocolMessage<RpcRequest>)ProtocolMessageDecoder.decode(buffer);
            } catch (Exception e) {
                log.error("TcpServerHandler handle error", e);
                throw new RuntimeException(e);
            }

            RpcRequest request = message.getBody();
            RpcResponse response = handleRpcRequest(request);

            ProtocolMessage.Header header = message.getHeader();
            header.setType((byte)ProtocolMessageTypeEnum.RESPONSE.getKey());

            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, response);

            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (Exception e) {
                log.error("encode error", e);
                throw new RuntimeException(e);
            }
        });

        netSocket.handler(bufferHandlerWrapper);
    }
}
