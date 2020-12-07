package com.thoughtworks.buddiee.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginUserDTO {

    @NotNull
    private String username;
    @NotNull
    private String password;
    private Boolean rememberMe;
}
