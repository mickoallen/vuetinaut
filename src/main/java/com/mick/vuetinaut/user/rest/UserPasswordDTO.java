package com.mick.vuetinaut.user.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserPasswordDTO {
    @NotNull
    @JsonProperty("user")
    private UserDTO userDTO;

    @NotBlank
    @Min(7) //lucky number 7
    private String password;

    public UserPasswordDTO() {
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
