package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.exceptions.ErrorHandler;
import com.mick.vuetinaut.security.PrincipalUtils;
import com.mick.vuetinaut.user.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Rest controller for users
 */
@Controller(UserController.USERS_ROUTE)
public class UserController {
    public static final String USERS_ROUTE = "/api/users";

    private final UserService userService;

    @Inject
    public UserController(
            final UserService userService) {
        this.userService = userService;
    }

    @Put(
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Single<MutableHttpResponse<UserDto>> createUser(final @Body @Valid UserPasswordDto userPasswordDTO) {
        return userService
                .createUser(UserMapper.toEntity(userPasswordDTO.getUserDto()), userPasswordDTO.getPassword())
                .map(UserMapper::toDto)
                .map(HttpResponse::created)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Get(
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<List<UserDto>>> searchByUsername(final @QueryValue("username") String username, final Principal principal) {
        return userService
                .searchByUsernameContains(username)
                .map(UserMapper::toDtos)
                .map(userDtos -> userDtos //filter current user for list
                        .stream()
                        .filter(userDto -> !userDto.getUuid().equals(PrincipalUtils.getUserUuid(principal)))
                        .collect(Collectors.toList())
                )
                .map(HttpResponse::ok)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Get(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<UserDto>> getUser(final @PathVariable("uuid") UUID uuid) {
        return userService
                .getUserFromUuid(uuid)
                .map(UserMapper::toDto)
                .map(HttpResponse::ok)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Get(
            value = "/current",
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<UserDto>> getCurrentUser(final Principal principal) {
        return getUser(PrincipalUtils.getUserUuid(principal));
    }


}
