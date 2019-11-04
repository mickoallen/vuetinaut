package com.mick.vuetinaut.user.rest;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

public class UserDto {
    private UUID uuid;

    @NotBlank
    private String username;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(uuid, userDto.uuid) &&
                Objects.equals(username, userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                '}';
    }
}
