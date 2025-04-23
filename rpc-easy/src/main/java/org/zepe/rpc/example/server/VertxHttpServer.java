package org.zepe.rpc.example.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.vertx.core.Vertx;

/**
 * @author zzpus
 * @datetime 2025/4/23 12:08
 * @description
 */
public class VertxHttpServer implements HttpServer {
    private static final Log log = LogFactory.get();

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new HttpServerHandler());

        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                log.info("Server is listening on port {}", port);
            } else {
                log.error("Failed to start server: {}", result.cause());
            }
        });
    }

}
