/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model;


import com.mick.vuetinaut.jooq.model.tables.Notepad;
import com.mick.vuetinaut.jooq.model.tables.NotepadUserShare;
import com.mick.vuetinaut.jooq.model.tables.User;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index NOTEPAD_PKEY = Indexes0.NOTEPAD_PKEY;
    public static final Index NOTEPAD_USER_SHARE_PKEY = Indexes0.NOTEPAD_USER_SHARE_PKEY;
    public static final Index USER_PKEY = Indexes0.USER_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index NOTEPAD_PKEY = Internal.createIndex("notepad_pkey", Notepad.NOTEPAD, new OrderField[] { Notepad.NOTEPAD.UUID }, true);
        public static Index NOTEPAD_USER_SHARE_PKEY = Internal.createIndex("notepad_user_share_pkey", NotepadUserShare.NOTEPAD_USER_SHARE, new OrderField[] { NotepadUserShare.NOTEPAD_USER_SHARE.UUID }, true);
        public static Index USER_PKEY = Internal.createIndex("user_pkey", User.USER, new OrderField[] { User.USER.UUID }, true);
    }
}
