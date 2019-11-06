package com.mick.vuetinaut.notepad.rest;

import com.mick.vuetinaut.exceptions.ErrorHandler;
import com.mick.vuetinaut.notepad.NotepadService;
import com.mick.vuetinaut.security.PrincipalUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Validated
@Controller(NotepadController.NOTEPADS_ROUTE)
public class NotepadController {
    public static final String NOTEPADS_ROUTE = "/api/notepads";

    private static final String NOTEPAD_UUID_PATH_VARIABLE = "notepadUuid";
    private final NotepadService notepadService;

    @Inject
    public NotepadController(
            final NotepadService notepadService) {
        this.notepadService = notepadService;
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<NotepadDto>> createNotepad(
            final @Body @Valid NotepadDto notepadDto,
            final Principal principal
    ) {
        return notepadService
                .createNotepad(NotepadMapper.toEntity(notepadDto), PrincipalUtils.getUserUuid(principal))
                .map(NotepadMapper::toDto)
                .map(HttpResponse::created)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Post(
            value = "/{" + NOTEPAD_UUID_PATH_VARIABLE + "}",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<NotepadDto>> editNotepad(
            final @PathVariable(NOTEPAD_UUID_PATH_VARIABLE) UUID notepadUuid,
            final @Body @Valid NotepadDto notepadDto,
            final Principal principal
    ) {
        //validate dto and path match TODO
        return notepadService
                .editNotepad(NotepadMapper.toEntity(notepadDto), PrincipalUtils.getUserUuid(principal))
                .map(NotepadMapper::toDto)
                .map(HttpResponse::ok)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Post(
            value = "/{" + NOTEPAD_UUID_PATH_VARIABLE + "}/share",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<String>> shareNotepad(
            final @PathVariable(NOTEPAD_UUID_PATH_VARIABLE) UUID notepadUuid,
            final @QueryValue("user") UUID shareWithUserUuid,
            final Principal principal
    ) {
        return notepadService
                .shareNotepad(notepadUuid, shareWithUserUuid, PrincipalUtils.getUserUuid(principal))
                .andThen(Single.just(HttpResponse.ok("OK")))
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Get(
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<List<NotepadDto>>> getNotepads(Principal principal) {
        return notepadService
                .getNotepadsForUser(PrincipalUtils.getUserUuid(principal))
                .map(NotepadMapper::toDtos)
                .map(HttpResponse::ok)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Get(
            value = "/{" + NOTEPAD_UUID_PATH_VARIABLE + "}",
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<NotepadDto>> getNotepad(@PathVariable(NOTEPAD_UUID_PATH_VARIABLE) UUID notepadUuid, Principal principal) {
        return notepadService
                .getNotepad(notepadUuid, PrincipalUtils.getUserUuid(principal))
                .map(NotepadMapper::toDto)
                .map(HttpResponse::ok)
                .onErrorResumeNext(ErrorHandler::handleError);
    }

    @Delete(
            value = "/{" + NOTEPAD_UUID_PATH_VARIABLE + "}",
            produces = MediaType.APPLICATION_JSON
    )
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<MutableHttpResponse<String>> deleteNotepad(@PathVariable(NOTEPAD_UUID_PATH_VARIABLE) UUID notepadUuid, Principal principal) {
        return notepadService
                .deleteNotepad(notepadUuid, PrincipalUtils.getUserUuid(principal))
                .andThen(Single.just(HttpResponse.ok("OK")))
                .onErrorResumeNext(ErrorHandler::handleError);
    }
}
