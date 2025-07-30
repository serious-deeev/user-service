package org.serious.dev.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.serious.dev.entity.User;
import org.serious.dev.grpc.UserResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperUnitTest {

    private UserMapper testUserMapper;

    @BeforeEach
    void initTestMapper() {
        testUserMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapUserToUserResponse() {
        User testUser = User.builder()
                .id(100500L)
                .username("John")
                .email("john@example.com")
                .createdAt(LocalDateTime.of(2025, 5, 14, 23, 55))
                .build();

        UserResponse expectedUserResponse = UserResponse.newBuilder()
                .setId(100500L)
                .setUsername("John")
                .build();

        UserResponse actualUserResponse = testUserMapper.userToUserResponse(testUser);
        assertThat(actualUserResponse).isEqualTo(expectedUserResponse);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertThat(testUserMapper.userToUserResponse(null)).isNull();
    }
}
