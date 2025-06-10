package org.serious.dev;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.serious.dev.grpc.UserServiceGrpc;

@RequiredArgsConstructor
public class TestGrpcClient {

    private final String testGrpcServerUrl;
    private final Integer testGrpcServerPort;

    public UserServiceGrpc.UserServiceBlockingStub createStubClient() {
        return UserServiceGrpc.newBlockingStub(getChannel());
    }

    private Channel getChannel() {
        return ManagedChannelBuilder.forAddress(testGrpcServerUrl, testGrpcServerPort)
                .usePlaintext()
                .build();
    }
}
