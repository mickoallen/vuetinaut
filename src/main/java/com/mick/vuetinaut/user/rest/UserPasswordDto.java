package com.mick.vuetinaut.user.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.validation.Validated;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public class UserPasswordDto {
    @NotNull
    @JsonProperty("user")
    private UserDto userDto;

    @NotBlank
    private String password;

    public UserPasswordDto() {
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public UserPasswordDto setUserDto(UserDto userDto) {
        this.userDto = userDto;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserPasswordDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
