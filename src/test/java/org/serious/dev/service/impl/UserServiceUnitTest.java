package org.serious.dev.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.entity.User;
import org.serious.dev.mapper.UserMapper;
import org.serious.dev.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    UserRepository fakeUserRepository;

    @Mock
    UserMapper fakeUserMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldGetUserById() {
        User testUser = new User(1L, "John", "john@mail.ru", LocalDateTime.now());
        doReturn(Optional.of(testUser))
                .when(fakeUserRepository)
                .findById(1L);

        UserResponseDto expectedResponseDto = UserResponseDto.builder()
                .id(1L)
                .username("John")
                .build();

        doReturn(expectedResponseDto)
                .when(fakeUserMapper)
                .userToUserResponseDto(testUser);

        UserResponseDto actualResponseDto = userService.getUserById(1L);

        assertThat(actualResponseDto).isNotNull();
        assertThat(actualResponseDto).isEqualTo(expectedResponseDto);
    }
}
