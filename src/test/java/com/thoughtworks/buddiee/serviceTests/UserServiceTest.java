package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.dto.UserDTO;
import com.thoughtworks.buddiee.mapper.UserMapper;
import com.thoughtworks.buddiee.repository.UserRepository;
import com.thoughtworks.buddiee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static UserService userService;

    @Mock
    private static UserRepository userRepository;

    @Mock
    private static PasswordEncoder passwordEncoder;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    void setUp() {
        initMocks(this);
        userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    void should_return_user_info_when_register_user_success() {
        UserDTO userDTO = UserDTO.builder()
                .username("mock name")
                .email("mock@tw.com")
                .password("password")
                .build();
        when(userRepository.findUserByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        UserDTO userRsp = userService.registerUser(userDTO);
        assertEquals("USER", userRsp.getRole());
        assertEquals("mock name", userRsp.getUsername());

    }
}
