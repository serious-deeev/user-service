package org.serious.dev.config;

import org.serious.dev.GrpcServerRunner;
import org.serious.dev.exception.UserGlobalExceptionInterceptor;
import org.serious.dev.mapper.UserMapper;
import org.serious.dev.service.UserGrpcService;
import org.serious.dev.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public UserGrpcService userGrpcService(UserMapper userMapper, UserService userService) {
        return new UserGrpcService(userMapper, userService);
    }

    @Bean
    public GrpcServerRunner grpcServerRunner(
            @Value("${grpc.server.port}") Integer grpcServerPort,
            UserGlobalExceptionInterceptor userGlobalExceptionInterceptor,
            UserGrpcService userGrpcService
    ) {
        return new GrpcServerRunner(grpcServerPort, userGlobalExceptionInterceptor, userGrpcService);
    }
}
