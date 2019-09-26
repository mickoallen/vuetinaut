package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.User;

public class UserMapper {
    public static UserDTO toDto(User user){
        return new UserDTO()
                .setUuid(user.getUuid())
                .setUsername(user.getUsername());
    }
}
