package org.serious.dev.service;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.serious.dev.grpc.UserServiceGrpc;
import org.serious.dev.mapper.UserMapper;

@AllArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public void getUser(
            UserRequest request,
            StreamObserver<UserResponse> responseObserver
    ) {
        UserResponseDto userResponseDto = userService.getUserById(request.getId());
        UserResponse userResponse = userMapper.userResponseDtoToUserResponse(userResponseDto);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
