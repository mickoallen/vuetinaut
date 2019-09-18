/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model;


import com.mick.vuetinaut.jooq.model.tables.Conversation;
import com.mick.vuetinaut.jooq.model.tables.Message;
import com.mick.vuetinaut.jooq.model.tables.User;
import com.mick.vuetinaut.jooq.model.tables.UserConversation;
import com.mick.vuetinaut.jooq.model.tables.records.ConversationRecord;
import com.mick.vuetinaut.jooq.model.tables.records.MessageRecord;
import com.mick.vuetinaut.jooq.model.tables.records.UserConversationRecord;
import com.mick.vuetinaut.jooq.model.tables.records.UserRecord;

import javax.annotation.Generated;

import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ConversationRecord> CONVERSATION_PKEY = UniqueKeys0.CONVERSATION_PKEY;
    public static final UniqueKey<MessageRecord> MESSAGE_PKEY = UniqueKeys0.MESSAGE_PKEY;
    public static final UniqueKey<UserRecord> USER_PKEY = UniqueKeys0.USER_PKEY;
    public static final UniqueKey<UserConversationRecord> USER_CONVERSATION_PKEY = UniqueKeys0.USER_CONVERSATION_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<ConversationRecord> CONVERSATION_PKEY = Internal.createUniqueKey(Conversation.CONVERSATION, "conversation_pkey", Conversation.CONVERSATION.UUID);
        public static final UniqueKey<MessageRecord> MESSAGE_PKEY = Internal.createUniqueKey(Message.MESSAGE, "message_pkey", Message.MESSAGE.UUID);
        public static final UniqueKey<UserRecord> USER_PKEY = Internal.createUniqueKey(User.USER, "user_pkey", User.USER.UUID);
        public static final UniqueKey<UserConversationRecord> USER_CONVERSATION_PKEY = Internal.createUniqueKey(UserConversation.USER_CONVERSATION, "user_conversation_pkey", UserConversation.USER_CONVERSATION.UUID);
    }
}
