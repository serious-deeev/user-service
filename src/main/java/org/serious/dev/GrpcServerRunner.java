package org.serious.dev;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.RequiredArgsConstructor;
import org.serious.dev.exception.UserGlobalExceptionInterceptor;
import org.serious.dev.service.UserGrpcService;
import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;

@RequiredArgsConstructor
public class GrpcServerRunner implements DisposableBean {

    private final Integer grpcServerPort;
    private final UserGlobalExceptionInterceptor userGlobalExceptionInterceptor;
    private final UserGrpcService userGrpcService;
    private Server server;

    // сервер включает сервис для работы grpc-рефлексии
    public void start() throws IOException {
        server = ServerBuilder.forPort(grpcServerPort)
                .intercept(userGlobalExceptionInterceptor)
                .addService(ProtoReflectionService.newInstance())
                .addService(userGrpcService)
                .build()
                .start();
    }

    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

    @Override
    public void destroy() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void destroyNow() {
        if (server != null) {
            server.shutdownNow();
        }
    }
}
