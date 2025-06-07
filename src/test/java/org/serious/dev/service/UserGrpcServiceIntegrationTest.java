package org.serious.dev.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.serious.dev.GrpcServerRunner;
import org.serious.dev.TestGrpcClient;
import org.serious.dev.UserServiceApplication;
import org.serious.dev.config.TestGrpcConfig;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.serious.dev.grpc.UserServiceGrpc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {UserServiceApplication.class, TestGrpcConfig.class})
class UserGrpcServiceIntegrationTest {

    private final GrpcServerRunner testServerRunner;
    private final TestGrpcClient testGrpcClient;

    @AfterAll
    void shutdownNowServer() {
        testServerRunner.destroyNow();
    }

    @Sql(statements = {
            "INSERT INTO user_post_storage.users(id, username, email, created_at)" +
            "VALUES (3, 'cole', 'cole@mail.ru', now())," +
            "       (4, 'jane', 'jane@gmail.com', now());"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void test() throws IOException {
        testServerRunner.start();

        UserServiceGrpc.UserServiceBlockingStub stubClient = testGrpcClient.createStubClient();
        UserResponse actualUserResponse = stubClient.getUser(UserRequest.newBuilder()
                .setId(4)
                .build());

        assertThat(actualUserResponse).isNotNull();
        assertThat(actualUserResponse.getUsername()).isEqualTo("jane");
    }
}
