package com.mick.vuetinaut.notepad.rest;

import java.util.Objects;
import java.util.UUID;

public class NoteDto {
    private UUID uuid;
    private UUID notepadUuid;
    private String body;
    private UUID creatorUserUuid;
    private UUID editorUserUuid;
    private long dateEdited;
    private long dateCreated;

    public UUID getUuid() {
        return uuid;
    }

    public NoteDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UUID getNotepadUuid() {
        return notepadUuid;
    }

    public NoteDto setNotepadUuid(UUID notepadUuid) {
        this.notepadUuid = notepadUuid;
        return this;
    }

    public String getBody() {
        return body;
    }

    public NoteDto setBody(String body) {
        this.body = body;
        return this;
    }

    public UUID getCreatorUserUuid() {
        return creatorUserUuid;
    }

    public NoteDto setCreatorUserUuid(UUID creatorUserUuid) {
        this.creatorUserUuid = creatorUserUuid;
        return this;
    }

    public UUID getEditorUserUuid() {
        return editorUserUuid;
    }

    public NoteDto setEditorUserUuid(UUID editorUserUuid) {
        this.editorUserUuid = editorUserUuid;
        return this;
    }

    public long getDateEdited() {
        return dateEdited;
    }

    public NoteDto setDateEdited(long dateEdited) {
        this.dateEdited = dateEdited;
        return this;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public NoteDto setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDto noteDTO = (NoteDto) o;
        return dateEdited == noteDTO.dateEdited &&
                dateCreated == noteDTO.dateCreated &&
                Objects.equals(uuid, noteDTO.uuid) &&
                Objects.equals(notepadUuid, noteDTO.notepadUuid) &&
                Objects.equals(body, noteDTO.body) &&
                Objects.equals(creatorUserUuid, noteDTO.creatorUserUuid) &&
                Objects.equals(editorUserUuid, noteDTO.editorUserUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, notepadUuid, body, creatorUserUuid, editorUserUuid, dateEdited, dateCreated);
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "uuid=" + uuid +
                ", notepadUuid=" + notepadUuid +
                ", body='" + body + '\'' +
                ", creatorUserUuid=" + creatorUserUuid +
                ", editorUserUuid=" + editorUserUuid +
                ", dateEdited=" + dateEdited +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
