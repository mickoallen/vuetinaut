package com.mick.vuetinaut.user;

import com.mick.vuetinaut.LoginDto;
import com.mick.vuetinaut.LoginResponseDto;
import com.mick.vuetinaut.user.rest.UserController;
import com.mick.vuetinaut.user.rest.UserDto;
import com.mick.vuetinaut.user.rest.UserPasswordDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class LoginService {
    @Inject
    @Client("/")
    private RxHttpClient client;

    public LoginResponseDto createUserAndLogin() {
        String username = "test-user-" + UUID.randomUUID();

        UserPasswordDto userPasswordDto = new UserPasswordDto()
                .setUserDto(new UserDto().setUsername(username))
                .setPassword("password");

        client.toBlocking().exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);

        HttpResponse<LoginResponseDto> loginResponse = client.toBlocking()
                .exchange(HttpRequest.POST("/login", new LoginDto().setUsername(username).setPassword("password")), LoginResponseDto.class);

        return loginResponse.getBody().get();
    }
}
