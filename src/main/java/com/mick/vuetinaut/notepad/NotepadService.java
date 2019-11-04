package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.AuthorizationException;
import com.mick.vuetinaut.jooq.model.tables.pojos.Note;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
public class NotepadService {

    private final NotepadRepository notepadRepository;
    private final TimeProvider timeProvider;

    @Inject
    public NotepadService(
            final NotepadRepository notepadRepository,
            final TimeProvider timeProvider) {
        this.notepadRepository = notepadRepository;
        this.timeProvider = timeProvider;
    }

    /**
     * @param userUuid
     * @return
     */
    public Single<List<NotepadLoaded>> getNotepadsForUser(final UUID userUuid) {
        return notepadRepository
                .getNotepadsForUser(userUuid)
                .flatMap(notepadRepository::loadNotepadsWithNotes);
    }

    /**
     * @param notepad
     * @param userUuid
     * @return
     */
    public Single<Notepad> createNotepad(Notepad notepad, UUID userUuid) {
        notepad.setDateCreated(Timestamp.from(Instant.now()));
        notepad.setCreatorUserUuid(userUuid);
        notepad.setUuid(UUID.randomUUID());

        return notepadRepository
                .create(notepad)
                .andThen(Single.just(notepad));
    }

    /**
     * @param notepadUuid
     * @param userUuid
     * @return
     */
    public Single<NotepadLoaded> getNotepad(UUID notepadUuid, UUID userUuid) {
        return notepadRepository.getNotepad(notepadUuid)
                .flatMap(notepad -> notepadRepository.loadNotepad(notepad, userUuid));
    }


    public Single<NotepadLoaded> editNotepad(Notepad notepadEdit, UUID userUuid) {
        return doesUserHasAccessToNotepad(notepadEdit.getUuid(), userUuid)
                .flatMap(userHasAccess -> {
                    if (!userHasAccess) {
                        return Single.error(new AuthorizationException("User does not have access to edit notepad"));
                    }
                    return notepadRepository.getNotepad(notepadEdit.getUuid());
                })
                .map(existingNotepad -> {
                    existingNotepad.setName(notepadEdit.getName());
                    return existingNotepad;
                })
                .flatMap(notepadRepository::saveNotepad)
                .flatMap(savedNotepad -> notepadRepository.loadNotepad(savedNotepad, userUuid));
    }

    /**
     * @param notepadUuid
     * @param userUuid
     * @return
     */
    public Completable deleteNotepad(UUID notepadUuid, UUID userUuid) {
        return notepadRepository.deleteNotepad(notepadUuid, userUuid);
    }

    /**
     * @param note
     * @param userUuid
     * @return
     */
    public Single<Note> createNote(Note note, UUID userUuid) {
        note.setUuid(UUID.randomUUID());
        note.setCreatorUserUuid(userUuid);
        note.setEditorUserUuid(userUuid);
        note.setDateCreated(Timestamp.from(timeProvider.now()));
        note.setDateEdited(Timestamp.from(timeProvider.now()));

        //check user has access to that notepad
        return doesUserHasAccessToNotepad(note.getNotepadUuid(), userUuid)
                .flatMap(userHasAccess -> {
                    if (userHasAccess) {
                        return notepadRepository.create(note);
                    }

                    return Single.error(new AuthorizationException("User does not have access to notepad"));
                });
    }

    /**
     * @param noteEdit
     * @param userUuid
     * @return
     */
    public Single<Note> editNote(Note noteEdit, UUID userUuid) {
        return doesUserHasAccessToNotepad(noteEdit.getNotepadUuid(), userUuid)
                .flatMap(userHasAccess -> {
                    if (!userHasAccess) {
                        return Single.error(new AuthorizationException("User does not have access to notepad"));
                    }
                    return notepadRepository.getNote(noteEdit.getUuid());
                })
                .map(existingNote -> {
                    existingNote.setBody(noteEdit.getBody());
                    existingNote.setDateEdited(Timestamp.from(timeProvider.now()));
                    return existingNote;
                })
                .flatMap(notepadRepository::saveNote);
    }

    /**
     * @param notepadUuid
     * @param userUuid
     * @return
     */
    private Single<Boolean> doesUserHasAccessToNotepad(UUID notepadUuid, UUID userUuid) {
        return notepadRepository
                .getNotepadsForUser(userUuid)
                .map(notepads -> notepads.stream().anyMatch(notepad -> notepad.getUuid().equals(notepadUuid)));
    }

    public Completable shareNotepad(UUID notepadUuid, UUID shareWithUserUuid, UUID userPerformingShareUuid) {
        return notepadRepository
                .getNotepad(notepadUuid)
                .map(notepad -> {
                    if (!notepad.getCreatorUserUuid().equals(userPerformingShareUuid)) {
                        throw new AuthorizationException("You cannot share a notepad you didn't create.");
                    }
                    return notepad;
                })
                .flatMapCompletable(notepad -> notepadRepository.shareNotepadWithUser(notepad, shareWithUserUuid));
    }

    public Completable deleteNote(UUID notepadUuid, UUID userUuid) {
        return  notepadRepository
                .deleteNote(notepadUuid, userUuid);
    }
}
