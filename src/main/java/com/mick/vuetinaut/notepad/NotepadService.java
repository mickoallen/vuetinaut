package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.AuthorizationException;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import io.reactivex.Completable;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
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
    public Single<List<Notepad>> getNotepadsForUser(final UUID userUuid) {
        return notepadRepository.getNotepadsForUser(userUuid);
    }

    /**
     * @param notepad
     * @param userUuid
     * @return
     */
    public Single<Notepad> createNotepad(Notepad notepad, UUID userUuid) {
        Timestamp now = Timestamp.from(timeProvider.now());
        notepad.setDateCreated(now);
        notepad.setCreatorUserUuid(userUuid);
        notepad.setUuid(UUID.randomUUID());
        notepad.setEditorUserUuid(userUuid);
        notepad.setDateEdited(now);

        return notepadRepository
                .create(notepad)
                .andThen(Single.just(notepad));
    }

    /**
     * @param notepadUuid
     * @param userUuid
     * @return
     */
    public Single<Notepad> getNotepad(UUID notepadUuid, UUID userUuid) {
        return notepadRepository.getNotepad(notepadUuid, userUuid);
    }

    /**
     *
     * @param notepadEdit
     * @param userUuid
     * @return
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
                .flatMap(notepadRepository::saveNotepad);
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
     *
     * @param notepadUuid
     * @param shareWithUserUuid
     * @param userPerformingShareUuid
     * @return
     */
    public Completable shareNotepad(UUID notepadUuid, UUID shareWithUserUuid, UUID userPerformingShareUuid) {
        return notepadRepository
                .getNotepad(notepadUuid, userPerformingShareUuid)
                .map(notepad -> {
                    if (!notepad.getCreatorUserUuid().equals(userPerformingShareUuid)) {
                        throw new AuthorizationException("You cannot share a notepad you didn't create.");
                    }
                    return notepad;
                })
                .flatMapCompletable(notepad -> notepadRepository.shareNotepadWithUser(notepad, shareWithUserUuid));
    }
}
