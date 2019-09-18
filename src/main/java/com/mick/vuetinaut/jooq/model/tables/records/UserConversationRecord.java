/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables.records;


import com.mick.vuetinaut.jooq.model.tables.UserConversation;

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
public class UserConversationRecord extends UpdatableRecordImpl<UserConversationRecord> implements Record4<UUID, UUID, UUID, Timestamp> {

    private static final long serialVersionUID = 845267494;

    /**
     * Setter for <code>public.user_conversation.uuid</code>.
     */
    public void setUuid(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.user_conversation.uuid</code>.
     */
    public UUID getUuid() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.user_conversation.conversation_uuid</code>.
     */
    public void setConversationUuid(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.user_conversation.conversation_uuid</code>.
     */
    public UUID getConversationUuid() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.user_conversation.user_uuid</code>.
     */
    public void setUserUuid(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.user_conversation.user_uuid</code>.
     */
    public UUID getUserUuid() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.user_conversation.date_created</code>.
     */
    public void setDateCreated(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.user_conversation.date_created</code>.
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
        return UserConversation.USER_CONVERSATION.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return UserConversation.USER_CONVERSATION.CONVERSATION_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field3() {
        return UserConversation.USER_CONVERSATION.USER_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return UserConversation.USER_CONVERSATION.DATE_CREATED;
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
        return getConversationUuid();
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
        return getConversationUuid();
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
    public UserConversationRecord value1(UUID value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserConversationRecord value2(UUID value) {
        setConversationUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserConversationRecord value3(UUID value) {
        setUserUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserConversationRecord value4(Timestamp value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserConversationRecord values(UUID value1, UUID value2, UUID value3, Timestamp value4) {
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
     * Create a detached UserConversationRecord
     */
    public UserConversationRecord() {
        super(UserConversation.USER_CONVERSATION);
    }

    /**
     * Create a detached, initialised UserConversationRecord
     */
    public UserConversationRecord(UUID uuid, UUID conversationUuid, UUID userUuid, Timestamp dateCreated) {
        super(UserConversation.USER_CONVERSATION);

        set(0, uuid);
        set(1, conversationUuid);
        set(2, userUuid);
        set(3, dateCreated);
    }
}
