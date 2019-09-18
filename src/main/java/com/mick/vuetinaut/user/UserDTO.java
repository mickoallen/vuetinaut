package com.mick.vuetinaut.user;

import java.util.UUID;

public class UserDTO {
    private UUID uuid;
    private String username;

    public UserDTO() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }
}
