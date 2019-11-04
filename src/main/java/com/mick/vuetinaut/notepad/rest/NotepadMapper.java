package com.mick.vuetinaut.notepad.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import com.mick.vuetinaut.notepad.NotepadLoaded;

import java.util.List;
import java.util.stream.Collectors;

public class NotepadMapper {
    public static NotepadDto toDto(Notepad notepad) {
        return new NotepadDto()
                .setUuid(notepad.getUuid())
                .setName(notepad.getName())
                .setUserUuid(notepad.getCreatorUserUuid())
                .setLastUpdatedTimestamp(notepad.getDateCreated().getTime());
    }

    public static NotepadDto toDto(NotepadLoaded notepadLoaded) {
        return toDto(notepadLoaded.getNotepad())
                .setNotes(NoteMapper.toDtos(notepadLoaded.getNotes()));
    }

    public static List<NotepadDto> toDtos(List<NotepadLoaded> notepadLoadeds) {
        return notepadLoadeds
                .stream()
                .map(NotepadMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Notepad toEntity(NotepadDto notepadDTO) {
        Notepad notepad = new Notepad();
        notepad.setName(notepadDTO.getName());
        notepad.setUuid(notepadDTO.getUuid());
        return notepad;
    }
}
