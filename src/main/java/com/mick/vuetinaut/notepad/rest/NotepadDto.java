package com.mick.vuetinaut.notepad.rest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NotepadDto {
    private UUID uuid;
    @NotNull
    @NotEmpty
    private String name;
    private UUID userUuid;
    private List<NoteDto> notes = new ArrayList<>();
    private long lastUpdatedTimestamp;
    private UUID lastUpdatedUser;

    public UUID getUuid() {
        return uuid;
    }

    public NotepadDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public NotepadDto setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public NotepadDto setName(String name) {
        this.name = name;
        return this;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public NotepadDto setNotes(List<NoteDto> notes) {
        this.notes = notes;
        return this;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public NotepadDto setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
        return this;
    }

    public UUID getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public NotepadDto setLastUpdatedUser(UUID lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
        return this;
    }

    @Override
    public String toString() {
        return "NotepadDto{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", userUuid=" + userUuid +
                ", notes=" + notes +
                ", lastUpdatedTimestamp=" + lastUpdatedTimestamp +
                ", lastUpdatedUser=" + lastUpdatedUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotepadDto that = (NotepadDto) o;
        return lastUpdatedTimestamp == that.lastUpdatedTimestamp &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(userUuid, that.userUuid) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(lastUpdatedUser, that.lastUpdatedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, userUuid, notes, lastUpdatedTimestamp, lastUpdatedUser);
    }
}
