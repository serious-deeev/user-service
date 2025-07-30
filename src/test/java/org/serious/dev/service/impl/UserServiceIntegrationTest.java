package org.serious.dev.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.serious.dev.UserServiceApplication;
import org.serious.dev.grpc.UserResponse;
import org.serious.dev.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor
@SpringBootTest(classes = UserServiceApplication.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceIntegrationTest {

    private final UserService testUserService;

    @Test
    @Sql(statements = {
            "INSERT INTO user_post_storage.users(id, username, email, created_at)" +
            "VALUES (1, 'john', 'john@mail.ru', now())," +
            "       (2, 'mark', 'test@gmail.com', now());"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldGetUserById() {
        UserResponse actualUserResponse = testUserService.getUserById(2L);

        assertThat(actualUserResponse).isNotNull();
        assertThat(actualUserResponse.getId()).isEqualTo(2L);
    }
}
