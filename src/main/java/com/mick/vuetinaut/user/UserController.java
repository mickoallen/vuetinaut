package com.mick.vuetinaut.user;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import javax.inject.Inject;
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
            value = "/",
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<List<UserDTO>> getUser() {
        return HttpResponse.ok(List.of());
    }

    @Get(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<UserDTO> getUser(@PathVariable("uuid") UUID uuid) {

        return HttpResponse.ok();
    }
}
