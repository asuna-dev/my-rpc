package org.zepe.rpc.server.http;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.server.HttpServer;

/**
 * @author zzpus
 * @datetime 2025/4/23 12:08
 * @description
 */
@Slf4j
public class VertxHttpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new HttpServerHandler());

        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                log.info("Server is listening on port {}", port);
            } else {
                log.error("Failed to start server", result.cause());
            }
        });
    }

}
