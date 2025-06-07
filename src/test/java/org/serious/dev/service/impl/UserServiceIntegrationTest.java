package org.serious.dev.service.impl;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.serious.dev.UserServiceApplication;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = UserServiceApplication.class)
class UserServiceIntegrationTest {

    private final UserService testUserService;

    @Sql(statements = {
            "INSERT INTO user_post_storage.users(id, username, email, created_at)" +
            "VALUES (1, 'john', 'john@mail.ru', now())," +
            "       (2, 'mark', 'test@gmail.com', now());"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void shouldGetUserById() {
        UserResponseDto actualUserResponseDto = testUserService.getUserById(2L);

        assertThat(actualUserResponseDto).isNotNull();
        assertThat(actualUserResponseDto.id()).isEqualTo(2L);
    }
}
