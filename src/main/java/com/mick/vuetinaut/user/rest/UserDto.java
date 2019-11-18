package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.user.UserType;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

public class UserDto {
    private UUID uuid;
    @NotBlank
    private String username;
    private UserType userType = UserType.NORMAL;

    public UserDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserDto setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(uuid, userDto.uuid) &&
                Objects.equals(username, userDto.username) &&
                userType == userDto.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, userType);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", userType=" + userType +
                '}';
    }
}
