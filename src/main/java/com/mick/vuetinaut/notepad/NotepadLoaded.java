package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.jooq.model.tables.pojos.Note;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;

import java.util.List;

public class NotepadLoaded {
    private Notepad notepad;
    private List<Note> notes;

    public Notepad getNotepad() {
        return notepad;
    }

    public NotepadLoaded setNotepad(Notepad notepad) {
        this.notepad = notepad;
        return this;
    }

    public NotepadLoaded addNote(Note note){
        this.notes.add(note);
        return this;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public NotepadLoaded setNotes(List<Note> notes) {
        this.notes = notes;
        return this;
    }
}
