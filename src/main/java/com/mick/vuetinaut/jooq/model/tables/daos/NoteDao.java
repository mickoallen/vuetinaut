/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables.daos;


import com.mick.vuetinaut.jooq.model.tables.Note;
import com.mick.vuetinaut.jooq.model.tables.records.NoteRecord;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


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
public class NoteDao extends DAOImpl<NoteRecord, com.mick.vuetinaut.jooq.model.tables.pojos.Note, UUID> {

    /**
     * Create a new NoteDao without any configuration
     */
    public NoteDao() {
        super(Note.NOTE, com.mick.vuetinaut.jooq.model.tables.pojos.Note.class);
    }

    /**
     * Create a new NoteDao with an attached configuration
     */
    public NoteDao(Configuration configuration) {
        super(Note.NOTE, com.mick.vuetinaut.jooq.model.tables.pojos.Note.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UUID getId(com.mick.vuetinaut.jooq.model.tables.pojos.Note object) {
        return object.getUuid();
    }

    /**
     * Fetch records that have <code>uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByUuid(UUID... values) {
        return fetch(Note.NOTE.UUID, values);
    }

    /**
     * Fetch a unique record that has <code>uuid = value</code>
     */
    public com.mick.vuetinaut.jooq.model.tables.pojos.Note fetchOneByUuid(UUID value) {
        return fetchOne(Note.NOTE.UUID, value);
    }

    /**
     * Fetch records that have <code>notepad_uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByNotepadUuid(UUID... values) {
        return fetch(Note.NOTE.NOTEPAD_UUID, values);
    }

    /**
     * Fetch records that have <code>body IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByBody(String... values) {
        return fetch(Note.NOTE.BODY, values);
    }

    /**
     * Fetch records that have <code>creator_user_uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByCreatorUserUuid(UUID... values) {
        return fetch(Note.NOTE.CREATOR_USER_UUID, values);
    }

    /**
     * Fetch records that have <code>editor_user_uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByEditorUserUuid(UUID... values) {
        return fetch(Note.NOTE.EDITOR_USER_UUID, values);
    }

    /**
     * Fetch records that have <code>date_edited IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByDateEdited(Timestamp... values) {
        return fetch(Note.NOTE.DATE_EDITED, values);
    }

    /**
     * Fetch records that have <code>date_created IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.Note> fetchByDateCreated(Timestamp... values) {
        return fetch(Note.NOTE.DATE_CREATED, values);
    }
}
