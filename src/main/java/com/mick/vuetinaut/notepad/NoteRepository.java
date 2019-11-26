package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.daos.NotepadDao;
import com.mick.vuetinaut.jooq.model.tables.daos.NotepadUserShareDao;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare;
import com.mick.vuetinaut.jooq.model.tables.records.NotepadRecord;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Data access layer for notes
 */
@Singleton
public class NoteRepository {
    private static final Logger logger = LoggerFactory.getLogger(NoteRepository.class);

    private final NotepadDao notepadDao;
    private final NotepadUserShareDao notepadUserShareDao;
    private final TimeProvider timeProvider;

    @Inject
    public NoteRepository(
            final NotepadDao notepadDao,
            final NotepadUserShareDao notepadUserShareDao,
            final TimeProvider timeProvider) {
        this.notepadDao = notepadDao;
        this.notepadUserShareDao = notepadUserShareDao;
        this.timeProvider = timeProvider;
    }

    /**
     * Create the given note.
     *
     * @param notepad {@link Notepad} to create.
     */
    public Completable create(Notepad notepad) {
        logger.trace("Creating note: {}", notepad);
        return Completable
                .fromAction(() -> notepadDao.insert(notepad))
                .doOnComplete(() -> logger.trace("Note created successfully"))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Get all notes accessible to a given user. This includes ones they own, and ones they have shared with them.
     *
     * @param userUuid {@link UUID} of the user to get notes for.
     * @return List of notes.
     */
    public Single<List<Notepad>> getNotepadsForUser(final UUID userUuid) {
        logger.trace("Getting notes for user: {}", userUuid);
        return Single
                .fromCallable(() -> notepadDao.configuration().dsl()
                        .select(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.fields())
                        .from(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD)
                        .leftOuterJoin(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE)
                        .on(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE.NOTEPAD_UUID
                                .eq(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.UUID))
                        .where(
                                com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.CREATOR_USER_UUID.eq(userUuid)
                                        .or(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE.USER_UUID.eq(userUuid))
                        )
                        .fetchInto(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD)
                        .map(record -> notepadDao.mapper().map(record))
                )
                .doOnSuccess(note -> logger.trace("Found notes: {}", note))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Get a single note for the specified user.
     *
     * @param notepadUuid {@link UUID} of the notepad to get.
     * @param userUuid
     * @return Request note.
     * @throws NotFoundException if the note doesn't exist or the user doesn't have permission to view it.
     */
    public Single<Notepad> getNotepad(UUID notepadUuid, UUID userUuid) {
        logger.trace("Getting note: {} for user: {}", notepadUuid, userUuid);
        return Single
                .fromCallable(() -> {
                    NotepadRecord notepad = notepadDao.configuration().dsl()
                            .select()
                            .from(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD)
                            .leftOuterJoin(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE)
                            .on(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE.NOTEPAD_UUID
                                    .eq(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.UUID))
                            .where(
                                    com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.UUID.eq(notepadUuid)
                                            .and(
                                                    com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD.CREATOR_USER_UUID.eq(userUuid)
                                                            .or(com.mick.vuetinaut.jooq.model.tables.NotepadUserShare.NOTEPAD_USER_SHARE.USER_UUID.eq(userUuid))
                                            )

                            )
                            .fetchSingleInto(com.mick.vuetinaut.jooq.model.tables.Notepad.NOTEPAD);

                    if (notepad == null) {
                        throw new NotFoundException("Notepad doesn't exist");
                    }

                    return notepadDao.mapper().map(notepad);
                })
                .doOnSuccess(notepad -> logger.trace("Got note: {}", notepad))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Save the given note.
     *
     * @param notepad Note to save.
     * @return The saved note.
     */
    public Single<Notepad> saveNotepad(Notepad notepad) {
        logger.trace("Saving note: {}", notepad);
        return Single
                .fromCallable(() -> {
                    notepadDao.update(notepad);

                    return notepad;
                })
                .doOnSuccess(savedNotepad -> logger.trace("Note saved: {}", savedNotepad))
                .subscribeOn(Schedulers.io());
    }


    /**
     * Delete the specified note for the given user. If the user is not the owner of the note,
     * any sharing association with the user is deleted instead.
     *
     * @param notepadUuid {@link UUID} of the note to be deleted.
     * @param userUuid {@link UUID} of the user deleting the note.
     * @throws NotFoundException if the note doesn't exist.
     */
    public Completable deleteNotepad(UUID notepadUuid, UUID userUuid) {
        logger.trace("Deleting note: {} by user: {}", notepadUuid, userUuid );
        return Completable
                .fromAction(() -> {
                    Notepad notepad = notepadDao.fetchOneByUuid(notepadUuid);

                    if (notepad == null) {
                        throw new NotFoundException("Note not found");
                    }

                    //actually delete if you own the note
                    if (notepad.getCreatorUserUuid().equals(userUuid)) {
                        logger.trace("User is owner of note, deleting the note");
                        notepadDao.delete(notepad);
                    }

                    logger.trace("removing share association for note");
                    notepadUserShareDao.delete(notepadUserShareDao.fetchByNotepadUuid(notepadUuid));
                })
                .doOnComplete(() -> logger.trace("Note deleted successfully"))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Share the given note with the specified user.
     *
     * @param notepad Note to share.
     * @param shareWithUserUuid {@link UUID} of the user to share the note with
     */
    public Completable shareNotepadWithUser(Notepad notepad, UUID shareWithUserUuid) {
        logger.trace("Sharing note: {} with user: {}", notepad, shareWithUserUuid);
        return Completable
                .fromAction(() -> {
                    NotepadUserShare notepadUserShare = new NotepadUserShare();
                    notepadUserShare.setNotepadUuid(notepad.getUuid());
                    notepadUserShare.setUserUuid(shareWithUserUuid);
                    notepadUserShare.setUuid(UUID.randomUUID());
                    notepadUserShare.setDateCreated(Timestamp.from(timeProvider.now()));
                    notepadUserShareDao.insert(notepadUserShare);
                })
                .doOnComplete(() -> logger.trace("Note shared successfully"))
                .subscribeOn(Schedulers.io());
    }
}
