package org.serious.dev.grpc.config;

import io.grpc.ServerInterceptor;
import org.serious.dev.GrpcServerRunner;
import org.serious.dev.service.UserGrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("prod")
@Configuration
public class GrpcConfig {

    @Bean
    public GrpcServerRunner grpcServerRunner(
            @Value("${grpc.server.port}") Integer grpcServerPort,
            List<ServerInterceptor> interceptors,
            UserGrpcService userGrpcService
    ) {
        return new GrpcServerRunner(grpcServerPort, interceptors, userGrpcService);
    }
}
