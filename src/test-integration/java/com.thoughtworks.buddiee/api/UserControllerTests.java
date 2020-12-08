package com.thoughtworks.buddiee.api;

import com.thoughtworks.buddiee.base.ApiBaseTest;
import com.thoughtworks.buddiee.dto.UserRequestDTO;
import com.thoughtworks.buddiee.repository.UserRepository;
import com.thoughtworks.buddiee.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static com.thoughtworks.buddiee.filter.JWTAuthenticationFilter.REDIS_AUTHENTICATION_HEADER;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserControllerTests extends ApiBaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisService redisService;

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

    @Test
    @WithMockUser(username = "mock@tw.com", password = "123")
    void should_remove_redis_cache_when_logout_user() {
        String mockUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        redisService.set(REDIS_AUTHENTICATION_HEADER + mockUsername, "mock token");

        givenMockMvc()
                .post("/api/auth/logout")
                .then()
                .statusCode(200);

        assertNull(redisService.get(REDIS_AUTHENTICATION_HEADER + mockUsername));
    }
}
