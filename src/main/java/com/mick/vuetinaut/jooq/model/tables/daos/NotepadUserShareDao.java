/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables.daos;


import com.mick.vuetinaut.jooq.model.tables.NotepadUserShare;
import com.mick.vuetinaut.jooq.model.tables.records.NotepadUserShareRecord;

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
public class NotepadUserShareDao extends DAOImpl<NotepadUserShareRecord, com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare, UUID> {

    /**
     * Create a new NotepadUserShareDao without any configuration
     */
    public NotepadUserShareDao() {
        super(NotepadUserShare.NOTEPAD_USER_SHARE, com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare.class);
    }

    /**
     * Create a new NotepadUserShareDao with an attached configuration
     */
    public NotepadUserShareDao(Configuration configuration) {
        super(NotepadUserShare.NOTEPAD_USER_SHARE, com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UUID getId(com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare object) {
        return object.getUuid();
    }

    /**
     * Fetch records that have <code>uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare> fetchByUuid(UUID... values) {
        return fetch(NotepadUserShare.NOTEPAD_USER_SHARE.UUID, values);
    }

    /**
     * Fetch a unique record that has <code>uuid = value</code>
     */
    public com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare fetchOneByUuid(UUID value) {
        return fetchOne(NotepadUserShare.NOTEPAD_USER_SHARE.UUID, value);
    }

    /**
     * Fetch records that have <code>notepad_uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare> fetchByNotepadUuid(UUID... values) {
        return fetch(NotepadUserShare.NOTEPAD_USER_SHARE.NOTEPAD_UUID, values);
    }

    /**
     * Fetch records that have <code>user_uuid IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare> fetchByUserUuid(UUID... values) {
        return fetch(NotepadUserShare.NOTEPAD_USER_SHARE.USER_UUID, values);
    }

    /**
     * Fetch records that have <code>date_created IN (values)</code>
     */
    public List<com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare> fetchByDateCreated(Timestamp... values) {
        return fetch(NotepadUserShare.NOTEPAD_USER_SHARE.DATE_CREATED, values);
    }
}