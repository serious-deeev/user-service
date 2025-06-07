package org.serious.dev.config;

import org.serious.dev.TestGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestGrpcConfig {

    @Bean
    public TestGrpcClient testGrpcClient(@Value("${grpc.server.url}") String testGrpcServerUrl,
                                         @Value("${grpc.server.port}") Integer testGrpcServerPort) {
        return new TestGrpcClient(testGrpcServerUrl, testGrpcServerPort);
    }
}
