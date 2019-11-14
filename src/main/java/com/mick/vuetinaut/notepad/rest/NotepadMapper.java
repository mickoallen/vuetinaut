package com.mick.vuetinaut.notepad.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;

import java.util.List;
import java.util.stream.Collectors;

public class NotepadMapper {
    public static NotepadDto toDto(Notepad notepad) {
        return new NotepadDto()
                .setUuid(notepad.getUuid())
                .setName(notepad.getName())
                .setBody(notepad.getBody())
                .setUserUuid(notepad.getCreatorUserUuid())
                .setLastUpdatedTimestamp(notepad.getDateEdited().getTime());
    }

    public static Notepad toEntity(NotepadDto notepadDTO) {
        Notepad notepad = new Notepad();
        notepad.setName(notepadDTO.getName());
        notepad.setUuid(notepadDTO.getUuid());
        notepad.setBody(notepadDTO.getBody() == null ? "" : notepadDTO.getBody());
        return notepad;
    }

    public static List<NotepadDto> toDtos(List<Notepad> notepads) {
        return notepads
                .stream()
                .map(NotepadMapper::toDto)
                .collect(Collectors.toList());
    }
}
