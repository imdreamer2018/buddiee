package com.thoughtworks.buddiee.controller;

import com.thoughtworks.buddiee.dto.UserRequestDTO;
import com.thoughtworks.buddiee.dto.UserResponseDTO;
import com.thoughtworks.buddiee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public UserResponseDTO registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return userService.registerUser(userRequestDTO);
    }
}
