package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.LoginResponseDto;
import com.mick.vuetinaut.notepad.rest.NoteDto;
import com.mick.vuetinaut.notepad.rest.NotepadController;
import com.mick.vuetinaut.notepad.rest.NotepadDto;
import com.mick.vuetinaut.user.LoginService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotepadControllerTest {
    @Inject
    @Client("/")
    private RxHttpClient client;

    @Inject
    private LoginService loginService;

    @Test
    public void createNotepad() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        NotepadDto notepadDto = new NotepadDto()
                .setName("Test notepad");

        HttpResponse<NotepadDto> notepadCreateResponse = client.toBlocking().exchange(
                HttpRequest.PUT(NotepadController.NOTEPADS_ROUTE, notepadDto).bearerAuth(accessToken), NotepadDto.class);

        assertThat(notepadCreateResponse.getStatus()).isEqualTo(HttpStatus.CREATED);

        NotepadDto createdNotepad = notepadCreateResponse.getBody().get();

        assertThat(createdNotepad.getName()).isEqualTo(notepadDto.getName());
        assertThat(createdNotepad.getLastUpdatedTimestamp()).isNotNull();
        assertThat(createdNotepad.getUserUuid()).isNotNull();
    }

    @Test
    public void createInvalidNotepad() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        NotepadDto notepadDto = new NotepadDto();

        try {
            client
                    .toBlocking()
                    .exchange(
                            HttpRequest
                                    .PUT(NotepadController.NOTEPADS_ROUTE, notepadDto)
                                    .bearerAuth(accessToken),
                            NotepadDto.class
                    );

        } catch (HttpClientResponseException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void editNotepad() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        NotepadDto notepadToUpdate = createNotepad("Test notepad to update", accessToken);

        HttpResponse<NotepadDto> notepadUpdateResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest
                                .POST(NotepadController.NOTEPADS_ROUTE + "/" + notepadToUpdate.getUuid(), notepadToUpdate.setName("An updated name"))
                                .bearerAuth(accessToken),
                        NotepadDto.class
                );

        assertThat(notepadUpdateResponse.getStatus()).isEqualTo(HttpStatus.OK);

        NotepadDto updatedNotepad = notepadUpdateResponse.getBody().get();

        assertThat(updatedNotepad.getName()).isEqualTo("An updated name");
    }

    @Test
    public void getNotepads() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        createNotepad("Test notepad 1", accessToken);
        createNotepad("Test notepad 2", accessToken);
        createNotepad("Test notepad 3", accessToken);

        HttpResponse<NotepadDtoList> notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(NotepadController.NOTEPADS_ROUTE).bearerAuth(accessToken),
                        NotepadDtoList.class
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);

        NotepadDtoList notepadDtos = notepadDtoListHttpResponse.body();

        assertThat(notepadDtos.size()).isEqualTo(3);
    }

    @Test
    public void getNotepad() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        NotepadDto createdNotepad = createNotepad("Test notepad 1", accessToken);

        HttpResponse<NotepadDto> notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid())
                                .bearerAuth(accessToken),
                        NotepadDto.class
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(notepadDtoListHttpResponse.body()).isEqualTo(createdNotepad);
    }

    @Test
    public void deleteNotepad() {
        String accessToken = loginService.createUserAndLogin().getAccess_token();

        NotepadDto createdNotepad = createNotepad("Test notepad 1", accessToken);

        HttpResponse notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.DELETE(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid())
                                .bearerAuth(accessToken)
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);

        try {
            client
                    .toBlocking()
                    .exchange(
                            HttpRequest.GET(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid())
                                    .bearerAuth(accessToken),
                            NotepadDto.class
                    );
        } catch (HttpClientResponseException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void deleteNotepadWithNote() {
        LoginResponseDto loginResponse = loginService.createUserAndLogin();

        NotepadDto createdNotepad = createNotepad("Test notepad", loginResponse.getAccess_token());

        NoteDto noteDtoToCreate = new NoteDto()
                .setBody("This is a cool note!")
                .setNotepadUuid(createdNotepad.getUuid());

        HttpResponse<NoteDto> createNoteResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.PUT(
                                NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes",
                                noteDtoToCreate
                        ).bearerAuth(loginResponse.getAccess_token()),
                        NoteDto.class
                );

        //delete notepad
        HttpResponse notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.DELETE(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid())
                                .bearerAuth(loginResponse.getAccess_token())
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);

        try {
            client
                    .toBlocking()
                    .exchange(
                            HttpRequest.GET(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes" +"/" + createNoteResponse.body().getUuid() )
                                    .bearerAuth(loginResponse.getAccess_token()),
                            NotepadDto.class
                    );
        } catch (HttpClientResponseException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void createNote() {
        LoginResponseDto loginResponse = loginService.createUserAndLogin();

        NotepadDto createdNotepad = createNotepad("Test notepad", loginResponse.getAccess_token());

        NoteDto noteDtoToCreate = new NoteDto()
                .setBody("This is a cool note!")
                .setNotepadUuid(createdNotepad.getUuid());

        HttpResponse<NoteDto> createNoteResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.PUT(
                                NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes",
                                noteDtoToCreate
                        ).bearerAuth(loginResponse.getAccess_token()),
                        NoteDto.class
                );

        assertThat(createNoteResponse.getStatus()).isEqualTo(HttpStatus.CREATED);

        NoteDto createdNoteDto = createNoteResponse.getBody().get();

        assertThat(createdNoteDto.getUuid()).isNotNull();
        assertThat(createdNoteDto.getNotepadUuid()).isEqualTo(createdNotepad.getUuid());
        assertThat(createdNoteDto.getBody()).isEqualTo(noteDtoToCreate.getBody());
        assertThat(createdNoteDto.getCreatorUserUuid()).isEqualTo(UUID.fromString(loginResponse.getUsername()));
    }

    @Test
    public void editNote() {
        LoginResponseDto loginResponse = loginService.createUserAndLogin();

        NotepadDto createdNotepad = createNotepad("Test notepad", loginResponse.getAccess_token());

        NoteDto noteDtoToCreate = new NoteDto()
                .setBody("This is a cool note!")
                .setNotepadUuid(createdNotepad.getUuid());

        NoteDto createdNoteDto = client
                .toBlocking()
                .exchange(
                        HttpRequest.PUT(
                                NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes",
                                noteDtoToCreate
                        ).bearerAuth(loginResponse.getAccess_token()),
                        NoteDto.class
                )
                .body();

        HttpResponse<NoteDto> editedNoteResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.POST(
                                NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes/" + createdNoteDto.getUuid(),
                                createdNoteDto.setBody("Updated body!")
                        ).bearerAuth(loginResponse.getAccess_token()),
                        NoteDto.class
                );


        assertThat(editedNoteResponse.getStatus()).isEqualTo(HttpStatus.OK);

        NoteDto editedNoteDto = editedNoteResponse.getBody().get();

        assertThat(editedNoteDto.getUuid()).isNotNull();
        assertThat(editedNoteDto.getNotepadUuid()).isEqualTo(createdNotepad.getUuid());
        assertThat(editedNoteDto.getBody()).isEqualTo("Updated body!");
        assertThat(editedNoteDto.getCreatorUserUuid()).isEqualTo(UUID.fromString(loginResponse.getUsername()));
        assertThat(editedNoteDto.getDateEdited()).isGreaterThan(createdNoteDto.getDateCreated());
    }

    @Test
    public void getLoadedNotepad() {
        LoginResponseDto loginResponse = loginService.createUserAndLogin();

        NotepadDto createdNotepad = createNotepad("Test notepad", loginResponse.getAccess_token());

        NoteDto noteDtoToCreate = new NoteDto()
                .setBody("This is a cool note!")
                .setNotepadUuid(createdNotepad.getUuid());

        client
                .toBlocking()
                .exchange(
                        HttpRequest.PUT(
                                NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid() + "/notes",
                                noteDtoToCreate
                        ).bearerAuth(loginResponse.getAccess_token()),
                        NoteDto.class
                );

        HttpResponse<NotepadDto> notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(NotepadController.NOTEPADS_ROUTE + "/" + createdNotepad.getUuid())
                                .bearerAuth(loginResponse.getAccess_token()),
                        NotepadDto.class
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);

        NotepadDto body = notepadDtoListHttpResponse.body();

        assertThat(body.getNotes().size()).isEqualTo(1);
    }

    @Test
    public void sharedNotepad() {
        String notepadOwnerToken = loginService.createUserAndLogin().getAccess_token();
        LoginResponseDto userToShareWith = loginService.createUserAndLogin();

        NotepadDto createdNotepad = createNotepad("Test notepad", notepadOwnerToken);

        String shareResponse = client.toBlocking()
                .exchange(
                        HttpRequest
                                .POST(
                                        String.format("%s/%s/share?user=%s", NotepadController.NOTEPADS_ROUTE, createdNotepad.getUuid(), userToShareWith.getUsername()),
                                        "{}"
                                )
                                .bearerAuth(notepadOwnerToken),
                        String.class)
                .body();

        HttpResponse<NotepadDtoList> notepadDtoListHttpResponse = client
                .toBlocking()
                .exchange(
                        HttpRequest.GET(NotepadController.NOTEPADS_ROUTE).bearerAuth(userToShareWith.getAccess_token()),
                        NotepadDtoList.class
                );

        assertThat(notepadDtoListHttpResponse.getStatus()).isEqualTo(HttpStatus.OK);

        NotepadDtoList notepadDtos = notepadDtoListHttpResponse.body();

        assertThat(notepadDtos.size()).isEqualTo(1);


    }

//
//    @Delete(
//            value = "/{" + NOTEPAD_UUID_PATH_VARIABLE + "}/notes/{" + NOTE_UUID_PATH_VARIABLE + "}",
//    )
//    public Single<MutableHttpResponse<Object>> deleteNote(
//    }

    private NotepadDto createNotepad(String name, String accessToken) {
        return client.toBlocking().exchange(
                HttpRequest
                        .PUT(
                                NotepadController.NOTEPADS_ROUTE,
                                new NotepadDto()
                                        .setName(name)
                        ).bearerAuth(accessToken), NotepadDto.class)
                .body();
    }

    //for typed deserialization
    private static class NotepadDtoList extends ArrayList<NotepadDto> {
    }
}
