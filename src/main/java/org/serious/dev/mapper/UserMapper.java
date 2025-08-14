package org.serious.dev.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.serious.dev.entity.User;
import org.serious.dev.grpc.UserNotificationDataResponse;
import org.serious.dev.grpc.UserResponse;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.username", target = "username")
    })
    UserResponse userToUserResponse(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.email", target = "email")
    })
    UserNotificationDataResponse userToUserNotificationDataResponse(User user);
}
