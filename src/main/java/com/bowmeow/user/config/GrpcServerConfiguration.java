package com.bowmeow.user.config;

import com.bowmeow.user.services.UserServer;
import com.bowmeow.user.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class GrpcServerConfiguration {

    @Value("${grpc.server.port}")
    private int grpcPort;

    private final UserServer userServer;

    public GrpcServerConfiguration(UserServer userServer) {
        this.userServer = userServer;
    }

    @Bean
    public UserServer userServer( UserService userService) {
        return new UserServer(userService, grpcPort);
    }

    @PostConstruct
    public void startGrpcServer() throws IOException {
        userServer.start();
    }
}
