/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables.records;


import com.mick.vuetinaut.jooq.model.tables.NotepadUserShare;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NotepadUserShareRecord extends UpdatableRecordImpl<NotepadUserShareRecord> implements Record4<UUID, UUID, UUID, Timestamp> {

    private static final long serialVersionUID = -1336484184;

    /**
     * Setter for <code>public.notepad_user_share.uuid</code>.
     */
    public void setUuid(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.notepad_user_share.uuid</code>.
     */
    public UUID getUuid() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.notepad_user_share.notepad_uuid</code>.
     */
    public void setNotepadUuid(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.notepad_user_share.notepad_uuid</code>.
     */
    public UUID getNotepadUuid() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.notepad_user_share.user_uuid</code>.
     */
    public void setUserUuid(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.notepad_user_share.user_uuid</code>.
     */
    public UUID getUserUuid() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.notepad_user_share.date_created</code>.
     */
    public void setDateCreated(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.notepad_user_share.date_created</code>.
     */
    public Timestamp getDateCreated() {
        return (Timestamp) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<UUID, UUID, UUID, Timestamp> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<UUID, UUID, UUID, Timestamp> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return NotepadUserShare.NOTEPAD_USER_SHARE.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return NotepadUserShare.NOTEPAD_USER_SHARE.NOTEPAD_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field3() {
        return NotepadUserShare.NOTEPAD_USER_SHARE.USER_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return NotepadUserShare.NOTEPAD_USER_SHARE.DATE_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component1() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component2() {
        return getNotepadUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component3() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value2() {
        return getNotepadUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value3() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotepadUserShareRecord value1(UUID value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotepadUserShareRecord value2(UUID value) {
        setNotepadUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotepadUserShareRecord value3(UUID value) {
        setUserUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotepadUserShareRecord value4(Timestamp value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotepadUserShareRecord values(UUID value1, UUID value2, UUID value3, Timestamp value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NotepadUserShareRecord
     */
    public NotepadUserShareRecord() {
        super(NotepadUserShare.NOTEPAD_USER_SHARE);
    }

    /**
     * Create a detached, initialised NotepadUserShareRecord
     */
    public NotepadUserShareRecord(UUID uuid, UUID notepadUuid, UUID userUuid, Timestamp dateCreated) {
        super(NotepadUserShare.NOTEPAD_USER_SHARE);

        set(0, uuid);
        set(1, notepadUuid);
        set(2, userUuid);
        set(3, dateCreated);
    }
}
