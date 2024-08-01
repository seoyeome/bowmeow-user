package com.bowmeow.user.services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserServer {
    private final int port;
    private final Server server;

    @Autowired
    public UserServer(UserService userService, int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(userService)
                .build();
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.error("*** shutting down gRPC server since JVM is shutting down");
                try {
                    UserServer.this.stop();
                } catch (InterruptedException e) {
                    log.error("Error while shutting down server", e);
                }
                log.error("*** server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}