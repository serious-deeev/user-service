package org.serious.dev.config;

import org.serious.dev.GrpcServerRunner;
import org.serious.dev.exception.UserGlobalExceptionInterceptor;
import org.serious.dev.service.UserGrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class GrpcConfig {

    @Bean
    public GrpcServerRunner grpcServerRunner(
            @Value("${grpc.server.port}") Integer grpcServerPort,
            UserGlobalExceptionInterceptor userGlobalExceptionInterceptor,
            UserGrpcService userGrpcService
    ) {
        return new GrpcServerRunner(grpcServerPort, userGlobalExceptionInterceptor, userGrpcService);
    }
}
