package com.mick.vuetinaut.notepad.rest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NotepadDto {
    private UUID uuid;
    @NotNull
    @NotEmpty
    private String name;
    private UUID userUuid;
    private String body;
    private long lastUpdatedTimestamp;
    private UUID lastUpdatedUser;
    private long createdTimestamp;
    private UUID owner;

    public UUID getUuid() {
        return uuid;
    }

    public NotepadDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public NotepadDto setName(String name) {
        this.name = name;
        return this;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public NotepadDto setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
        return this;
    }

    public String getBody() {
        return body;
    }

    public NotepadDto setBody(String body) {
        this.body = body;
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

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public NotepadDto setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public UUID getOwner() {
        return owner;
    }

    public NotepadDto setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotepadDto that = (NotepadDto) o;
        return lastUpdatedTimestamp == that.lastUpdatedTimestamp &&
                createdTimestamp == that.createdTimestamp &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(userUuid, that.userUuid) &&
                Objects.equals(body, that.body) &&
                Objects.equals(lastUpdatedUser, that.lastUpdatedUser) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, userUuid, body, lastUpdatedTimestamp, lastUpdatedUser, createdTimestamp, owner);
    }

    @Override
    public String toString() {
        return "NotepadDto{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", userUuid=" + userUuid +
                ", body='" + body + '\'' +
                ", lastUpdatedTimestamp=" + lastUpdatedTimestamp +
                ", lastUpdatedUser=" + lastUpdatedUser +
                ", createdTimestamp=" + createdTimestamp +
                ", owner=" + owner +
                '}';
    }
}
