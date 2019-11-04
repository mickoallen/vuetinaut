package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(User user){
        return new UserDto()
                .setUuid(user.getUuid())
                .setUsername(user.getUsername());
    }

    public static List<UserDto> toDtos(List<User> users) {
        return users
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static User toEntity(UserDto userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        return user;
    }
}
