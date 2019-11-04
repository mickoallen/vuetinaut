package com.mick.vuetinaut.notepad.rest;

import com.mick.vuetinaut.jooq.model.tables.pojos.Note;

import java.util.List;
import java.util.stream.Collectors;

public class NoteMapper {
    public static NoteDto toDto(Note note) {
        return new NoteDto()
                .setBody(note.getBody())
                .setCreatorUserUuid(note.getCreatorUserUuid())
                .setEditorUserUuid(note.getEditorUserUuid())
                .setDateCreated(note.getDateCreated().getTime())
                .setDateEdited(note.getDateEdited().getTime())
                .setNotepadUuid(note.getNotepadUuid())
                .setUuid(note.getUuid());
    }

    public static List<NoteDto> toDtos(List<Note> notes) {
        return notes
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Note toEntity(NoteDto noteDTO){
        Note note = new Note();
        note.setUuid(noteDTO.getUuid());
        note.setBody(noteDTO.getBody());
        note.setNotepadUuid(noteDTO.getNotepadUuid());
        return note;
    }
}
