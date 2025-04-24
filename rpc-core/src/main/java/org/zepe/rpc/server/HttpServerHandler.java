package org.zepe.rpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.registry.LocalRegistry;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author zzpus
 * @datetime 2025/4/23 20:04
 * @description
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        log.info("HttpRequest: {}: {}", request.method(), request.uri());
        if (!"/rpc".equals(request.uri())) {
            log.error("unsupported request");
            request.response().setStatusCode(403).end("bad request");
            return;
        }

        final Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());

        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            RpcResponse rpcResponse = new RpcResponse();

            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                log.error("deserialize error", e);
                rpcResponse.setException(e);
                rpcResponse.setMessage("deserialize body to RpcRequest failed");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            if (rpcRequest == null) {
                rpcResponse.setMessage("RpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            Class<?> svcClass = LocalRegistry.getService(rpcRequest.getServiceName());
            try {
                Method method = svcClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(svcClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                log.error("invoke service error", e);
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(request, rpcResponse, serializer);
        });

    }

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        log.info("RpcResponse: {}", rpcResponse);
        HttpServerResponse response = request.response().putHeader("content-type", "application/json");

        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            log.error("response error", e);
            response.end(Buffer.buffer());
        }

    }
}
