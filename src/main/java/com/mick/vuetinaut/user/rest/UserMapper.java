package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import com.mick.vuetinaut.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    public static UserDto toDto(User user) {
        return new UserDto()
                .setUuid(user.getUuid())
                .setUserType(getUserTypeFromString(user.getUserType()))
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
        user.setUserType(userDTO.getUserType().name());
        return user;
    }

    public static UserType getUserTypeFromString(String userTypeString) {
        try {
            return UserType.valueOf(userTypeString);
        } catch (Exception e) {
            logger.error("User type on entity is not valid: {}", userTypeString, e);
            return UserType.NORMAL;
        }
    }
}
