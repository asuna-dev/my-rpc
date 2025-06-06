package org.zepe.rpc.server.http;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.model.RpcStatusCode;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;
import org.zepe.rpc.server.RpcRequestHandler;

import java.io.IOException;

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
            RpcResponse rpcResponse;

            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                log.error("deserialize error", e);
                rpcResponse = RpcResponse.failure(RpcStatusCode.BAD_REQUEST, e, "deserialize RpcRequest error");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            if (rpcRequest == null) {
                rpcResponse = RpcResponse.failure(RpcStatusCode.BAD_REQUEST, "RpcRequest is null");

            } else {
                rpcResponse = RpcRequestHandler.handleRpcRequest(rpcRequest);
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
