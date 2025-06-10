package org.serious.dev.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.entity.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperUnitTest {

    private UserMapper testUserMapper;

    @BeforeEach
    void initTestMapper() {
        testUserMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapUserToUserResponseDto() {
        User testUser = User.builder()
                .id(100500L)
                .username("John")
                .email("john@example.com")
                .createdAt(LocalDateTime.of(2025, 5, 14, 23, 55))
                .build();

        UserResponseDto expectedUserResponseDto = UserResponseDto.builder()
                .id(100500L)
                .username("John")
                .build();

        UserResponseDto actualUserResponseDto = testUserMapper.userToUserResponseDto(testUser);
        assertThat(actualUserResponseDto).isEqualTo(expectedUserResponseDto);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertThat(testUserMapper.userToUserResponseDto(null)).isNull();
    }
}
