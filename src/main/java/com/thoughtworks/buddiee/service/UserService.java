package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.UserRequestDTO;
import com.thoughtworks.buddiee.dto.UserResponseDTO;
import com.thoughtworks.buddiee.entity.User;
import com.thoughtworks.buddiee.exception.BadRequestException;
import com.thoughtworks.buddiee.mapper.UserMapper;
import com.thoughtworks.buddiee.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findUserByUsername(userRequestDTO.getUsername()).isPresent() ||
            userRepository.findUserByEmail(userRequestDTO.getEmail()).isPresent())
            throw new BadRequestException("the user name have been register!");
        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .role(userRequestDTO.getRole() == null ? "USER" : userRequestDTO.getRole())
                .build();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }
}
