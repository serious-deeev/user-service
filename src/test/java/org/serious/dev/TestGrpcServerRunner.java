package org.serious.dev;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.RequiredArgsConstructor;
import org.serious.dev.service.UserGrpcService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TestGrpcServerRunner {

    private final Integer grpcServerPort;
    private final List<ServerInterceptor> interceptors;
    private final UserGrpcService userGrpcService;
    private Server server;

    /**
     * 1. gRPC вызывает интерцепторы в обратном порядке их добавления.
     * 2. Интерцептор, наполняющий контекст, должен вызываться первым.
     * 3. Сервер также включает сервис для работы grpc-рефлексии.
     */
    public void start() throws IOException {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerPort);
        interceptors.forEach(serverInterceptor -> serverBuilder.intercept(serverInterceptor));
        server = serverBuilder
                .addService(ProtoReflectionService.newInstance())
                .addService(ServerInterceptors.intercept(userGrpcService, interceptors))
                .build()
                .start();
    }

    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

    public void destroyNow() {
        if (server != null) {
            server.shutdownNow();
        }
    }
}
