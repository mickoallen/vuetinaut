package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.AuthorizationException;
import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import io.reactivex.Completable;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Service class for notes.
 * This is where the business logic for notes lives.
 */
@Singleton
public class NoteService {

    private final NoteRepository noteRepository;
    private final TimeProvider timeProvider;

    @Inject
    public NoteService(
            final NoteRepository noteRepository,
            final TimeProvider timeProvider) {
        this.noteRepository = noteRepository;
        this.timeProvider = timeProvider;
    }

    /**
     * Get the list of notes associated with a user. Inlcudes both owner and shared.
     *
     * @param userUuid {@link UUID} of user to fetch notes for.
     * @return List of notes.
     */
    public Single<List<Notepad>> getNotepadsForUser(final UUID userUuid) {
        return noteRepository.getNotepadsForUser(userUuid);
    }

    /**
     * Create a new note for the given user.
     *
     * @param notepad  Note to create
     * @param userUuid User creating the note
     * @return The created note.
     */
    public Single<Notepad> createNotepad(Notepad notepad, UUID userUuid) {
        Timestamp now = Timestamp.from(timeProvider.now());

        notepad.setUuid(UUID.randomUUID());
        notepad.setDateCreated(now);
        notepad.setCreatorUserUuid(userUuid);
        notepad.setEditorUserUuid(userUuid);
        notepad.setDateEdited(now);

        return noteRepository
                .create(notepad)
                .andThen(Single.just(notepad));
    }

    /**
     * Get a single note for a single user.
     *
     * @param notepadUuid {@link UUID} of the note to get.
     * @param userUuid    {@link UUID} of the user getting the note.
     * @return The request note.
     * @throws NotFoundException if the notepad doesn't exist,
     *                           or the user doesnt have access to it.
     */
    public Single<Notepad> getNotepad(UUID notepadUuid, UUID userUuid) {
        return noteRepository.getNotepad(notepadUuid, userUuid);
    }

    /**
     * Edit a single note by a single user
     *
     * @param notepadEdit The edited note, only editable fields will be updated.
     * @param userUuid    {@link UUID} of the user making the edit
     * @return The edited note
     * @throws NotFoundException if the notepad doesn't exist,
     *                           or the user doesnt have access to it.
     */
    public Single<Notepad> editNotepad(Notepad notepadEdit, UUID userUuid) {
        return getNotepad(notepadEdit.getUuid(), userUuid)
                .map(existingNotepad -> {
                    existingNotepad.setName(notepadEdit.getName());
                    existingNotepad.setBody(notepadEdit.getBody());
                    existingNotepad.setEditorUserUuid(userUuid);
                    existingNotepad.setDateEdited(Timestamp.from(timeProvider.now()));
                    return existingNotepad;
                })
                .flatMap(noteRepository::saveNotepad);
    }

    /**
     * Delete a given note by a given user. If if it is a shared note, it will remove
     * the association with the given user.
     *
     * @param notepadUuid {@link UUID} of the note to delete.
     * @param userUuid    {@link UUID} of the user performing the delete,
     * @throws NotFoundException if the notepad doesn't exist.
     */
    public Completable deleteNotepad(UUID notepadUuid, UUID userUuid) {
        return noteRepository.deleteNotepad(notepadUuid, userUuid);
    }

    /**
     * Share a note with a given user.
     *
     * @param notepadUuid             {@link UUID} of note to be shared.
     * @param shareWithUserUuid       {@link UUID} of user to share the note with.
     * @param userPerformingShareUuid {@link UUID} of the user performing the share.
     * @throws NotFoundException      if the note doesn't exist.
     * @throws AuthorizationException if the user isn't the creator of the note they are trying to share.
     */
    public Completable shareNotepad(UUID notepadUuid, UUID shareWithUserUuid, UUID userPerformingShareUuid) {
        return noteRepository
                .getNotepad(notepadUuid, userPerformingShareUuid)
                .map(notepad -> {
                    if (!notepad.getCreatorUserUuid().equals(userPerformingShareUuid)) {
                        throw new AuthorizationException("You cannot share a notepad you didn't create.");
                    }
                    return notepad;
                })
                .flatMapCompletable(notepad -> noteRepository.shareNotepadWithUser(notepad, shareWithUserUuid));
    }
}
