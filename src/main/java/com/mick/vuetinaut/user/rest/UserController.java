package com.mick.vuetinaut.user.rest;

import com.mick.vuetinaut.user.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller("/users")
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(
            final UserService userService) {
        this.userService = userService;
    }

    @Get(
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<HttpResponse<List<UserDTO>>> getAllUsers(Principal principal) {
        return Single.just(HttpResponse.ok(List.of()));
    }

    @Get(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<HttpResponse<UserDTO>> getUser(@PathVariable("uuid") UUID uuid) {
        return Single.just(HttpResponse.ok());
    }

    @Post(
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Single<HttpResponse<UserDTO>> createUser(@Body UserPasswordDTO userPasswordDTO) {
        return Single.just(HttpResponse.ok());
    }
}
