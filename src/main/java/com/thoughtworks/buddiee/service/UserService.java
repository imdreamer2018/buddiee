package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.UserDTO;
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

    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findUserByUsername(userDTO.getUsername()).isPresent() ||
            userRepository.findUserByEmail(userDTO.getEmail()).isPresent())
            throw new BadRequestException("the user name have been register!");
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole() == null ? "USER" : userDTO.getRole())
                .build();
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
}
