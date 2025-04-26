package org.zepe.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.server.HttpServer;

/**
 * @author zzpus
 * @datetime 2025/4/25 23:22
 * @description
 */
@Slf4j
public class VertxTcpServer implements HttpServer {
    private byte[] handleRequest(byte[] requestBytes) {
        return "hi".getBytes();
    }

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        NetServer server = vertx.createNetServer();

        server.connectHandler(new TcpServerHandler());

        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("VertxTcpServer listening on {}", port);
            } else {
                log.error("VertxTcpServer start failed", result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(9898);
    }

}
