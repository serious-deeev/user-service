package org.serious.dev.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.entity.User;
import org.serious.dev.grpc.UserResponse;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    UserResponseDto userToUserResponseDto(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(source = "userResponseDto.id", target = "id"),
            @Mapping(source = "userResponseDto.username", target = "username")
    })
    UserResponse userResponseDtoToUserResponse(UserResponseDto userResponseDto);
}
