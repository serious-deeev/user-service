package org.serious.dev.grpc.config;

import io.grpc.ServerInterceptor;
import org.serious.dev.TestGrpcClient;
import org.serious.dev.TestGrpcServerRunner;
import org.serious.dev.service.UserGrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@TestConfiguration
public class TestGrpcConfig {

    @Bean
    public TestGrpcServerRunner testGrpcServerRunner(
            @Value("${grpc.server.port}") Integer grpcServerPort,
            List<ServerInterceptor> interceptors,
            UserGrpcService userGrpcService
    ) {
        return new TestGrpcServerRunner(grpcServerPort, interceptors, userGrpcService);
    }

    @Bean
    public TestGrpcClient testGrpcClient(@Value("${grpc.server.url}") String testGrpcServerUrl,
                                         @Value("${grpc.server.port}") Integer testGrpcServerPort) {
        return new TestGrpcClient(testGrpcServerUrl, testGrpcServerPort);
    }
}
