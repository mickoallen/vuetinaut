package com.mick.vuetinaut.user;

import com.mick.vuetinaut.LoginResponseDto;
import com.mick.vuetinaut.user.rest.UserController;
import com.mick.vuetinaut.user.rest.UserDto;
import com.mick.vuetinaut.user.rest.UserPasswordDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Inject
    @Client("/")
    private RxHttpClient client;

    @Inject
    private LoginService loginService;

    private String accessToken;

    @BeforeAll
    public void init() {
        accessToken = loginService.createUserAndLogin().getAccess_token();
    }

    @Test
    public void createUser() {
        UserPasswordDto userPasswordDto = new UserPasswordDto()
                .setUserDto(new UserDto().setUsername("mick"))
                .setPassword("mickiscool");

        HttpResponse<UserDto> response = client.toBlocking().exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);

        UserDto userDto = response.getBody().get();

        assertThat(userDto.getUsername()).isEqualTo(userPasswordDto.getUserDto().getUsername());
        assertThat(userDto.getUuid()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createDuplicateUser() {
        UserPasswordDto userPasswordDto = new UserPasswordDto()
                .setUserDto(new UserDto().setUsername("mickDuplicate"))
                .setPassword("mickiscool");

        client.toBlocking().exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);

        try {
            client
                    .toBlocking()
                    .exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);
        } catch (HttpClientResponseException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Test
    public void searchByUsername() {
        UserPasswordDto userPasswordDto = new UserPasswordDto()
                .setUserDto(new UserDto().setUsername("uniqueStringToSearch"))
                .setPassword("mickiscool");

        HttpResponse<UserDto> response = client.toBlocking().exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);


        HttpResponse<UserDtoList> userSearch = client.toBlocking().exchange(
                HttpRequest
                        .GET(UserController.USERS_ROUTE + "?username=uniqueString")
                        .bearerAuth(accessToken),
                UserDtoList.class);

        UserDtoList userDtos = userSearch.getBody().get();

        assertThat(userDtos.size()).isEqualTo(1);
        assertThat(userDtos.get(0).getUsername()).isEqualTo("uniqueStringToSearch");
    }

    @Test
    public void getUser() {
        UserPasswordDto userPasswordDto = new UserPasswordDto()
                .setUserDto(new UserDto().setUsername("mick2fetch"))
                .setPassword("mickiscool");

        HttpResponse<UserDto> response = client.toBlocking().exchange(HttpRequest.PUT(UserController.USERS_ROUTE, userPasswordDto), UserDto.class);

        UUID userUuid = response.getBody().get().getUuid();

        HttpResponse<UserDto> getUserResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(UserController.USERS_ROUTE + "/" + userUuid)
                                .bearerAuth(accessToken),
                        UserDto.class
                );

        UserDto userDto = getUserResponse.getBody().get();

        assertThat(userDto.getUsername()).isEqualTo("mick2fetch");
    }

    @Test
    public void getCurrentUser() {
        LoginResponseDto loginResponse = loginService.createUserAndLogin();

        UserDto createdUserDto = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(
                                UserController.USERS_ROUTE + "/" + loginResponse.getUsername()
                        ).bearerAuth(loginResponse.getAccess_token()),
                        UserDto.class
                )
                .body();

        UserDto currentUserDto = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(
                                UserController.USERS_ROUTE + "/current"
                        ).bearerAuth(loginResponse.getAccess_token()),
                        UserDto.class
                )
                .body();

        assertThat(createdUserDto).isEqualTo(currentUserDto);
    }

    /*for typed deserialization*/
    private static class UserDtoList extends ArrayList<UserDto> {
    }
}
