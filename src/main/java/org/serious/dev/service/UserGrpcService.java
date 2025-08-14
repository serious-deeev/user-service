package org.serious.dev.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.serious.dev.grpc.UserNotificationDataRequest;
import org.serious.dev.grpc.UserNotificationDataResponse;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.serious.dev.grpc.UserServiceGrpc;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse userResponse = userService.getUserById(request.getId());

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserNotificationData(UserNotificationDataRequest request, StreamObserver<UserNotificationDataResponse> responseObserver) {
        UserNotificationDataResponse userNotificationDataResponse = userService.getUserNotificationDataById(request.getId());

        responseObserver.onNext(userNotificationDataResponse);
        responseObserver.onCompleted();
    }
}
