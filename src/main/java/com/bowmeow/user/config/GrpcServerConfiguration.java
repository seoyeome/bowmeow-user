package com.bowmeow.user.config;

import com.bowmeow.user.services.UserServer;
import com.bowmeow.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfiguration {

    @Value( "${grpc.server.port}" )
    private int grpcPort;

    private final UserService userService;
    private final UserServer userServer;

    @PostConstruct
    public void startGrpcServer() throws IOException {
        userServer.setPort( grpcPort );
        userServer.initialize( userService );
        userServer.start();
    }
}
