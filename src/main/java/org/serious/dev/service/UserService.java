package org.serious.dev.service;

import org.serious.dev.grpc.UserNotificationDataResponse;
import org.serious.dev.grpc.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);

    UserNotificationDataResponse getUserNotificationDataById(Long id);
}
