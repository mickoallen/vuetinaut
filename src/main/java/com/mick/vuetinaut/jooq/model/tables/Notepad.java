/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables;


import com.mick.vuetinaut.jooq.model.Indexes;
import com.mick.vuetinaut.jooq.model.Keys;
import com.mick.vuetinaut.jooq.model.Public;
import com.mick.vuetinaut.jooq.model.tables.records.NotepadRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class Notepad extends TableImpl<NotepadRecord> {

    private static final long serialVersionUID = 1383519884;

    /**
     * The reference instance of <code>public.notepad</code>
     */
    public static final Notepad NOTEPAD = new Notepad();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NotepadRecord> getRecordType() {
        return NotepadRecord.class;
    }

    /**
     * The column <code>public.notepad.uuid</code>.
     */
    public final TableField<NotepadRecord, UUID> UUID = createField("uuid", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.notepad.name</code>.
     */
    public final TableField<NotepadRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.notepad.creator_user_uuid</code>.
     */
    public final TableField<NotepadRecord, UUID> CREATOR_USER_UUID = createField("creator_user_uuid", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.notepad.date_created</code>.
     */
    public final TableField<NotepadRecord, Timestamp> DATE_CREATED = createField("date_created", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>public.notepad</code> table reference
     */
    public Notepad() {
        this(DSL.name("notepad"), null);
    }

    /**
     * Create an aliased <code>public.notepad</code> table reference
     */
    public Notepad(String alias) {
        this(DSL.name(alias), NOTEPAD);
    }

    /**
     * Create an aliased <code>public.notepad</code> table reference
     */
    public Notepad(Name alias) {
        this(alias, NOTEPAD);
    }

    private Notepad(Name alias, Table<NotepadRecord> aliased) {
        this(alias, aliased, null);
    }

    private Notepad(Name alias, Table<NotepadRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Notepad(Table<O> child, ForeignKey<O, NotepadRecord> key) {
        super(child, key, NOTEPAD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.NOTEPAD_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<NotepadRecord> getPrimaryKey() {
        return Keys.NOTEPAD_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<NotepadRecord>> getKeys() {
        return Arrays.<UniqueKey<NotepadRecord>>asList(Keys.NOTEPAD_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notepad as(String alias) {
        return new Notepad(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notepad as(Name alias) {
        return new Notepad(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Notepad rename(String name) {
        return new Notepad(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Notepad rename(Name name) {
        return new Notepad(name, null);
    }
}
