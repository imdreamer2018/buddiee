package com.thoughtworks.buddiee.api;

import com.thoughtworks.buddiee.base.ApiBaseTest;
import com.thoughtworks.buddiee.dto.UserRequestDTO;
import com.thoughtworks.buddiee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;

class UserControllerTests extends ApiBaseTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void should_return_user_info_when_register_user_success() {
        UserRequestDTO userRequestDTO = UserRequestDTO
                .builder()
                .username("mock username")
                .email("mock@tw.com")
                .password("password")
                .build();
        givenMockMvc()
                .body(userRequestDTO)
                .post("/api/auth/register")
                .then()
                .statusCode(201)
                .body("role", is("USER"))
                .body("username", is("mock username"));
    }
}
