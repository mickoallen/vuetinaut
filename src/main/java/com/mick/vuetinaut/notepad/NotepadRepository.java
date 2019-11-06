package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.AuthorizationException;
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

@Singleton
public class NotepadRepository {
    private static final Logger logger = LoggerFactory.getLogger(NotepadRepository.class);

    private final NotepadDao notepadDao;
    private final NotepadUserShareDao notepadUserShareDao;
    private final TimeProvider timeProvider;

    @Inject
    public NotepadRepository(
            final NotepadDao notepadDao,
            final NotepadUserShareDao notepadUserShareDao,
            final TimeProvider timeProvider) {
        this.notepadDao = notepadDao;
        this.notepadUserShareDao = notepadUserShareDao;
        this.timeProvider = timeProvider;
    }

    public Completable create(Notepad notepad) {
        return Completable.fromAction(() -> notepadDao.insert(notepad));
    }

    public Single<List<Notepad>> getNotepadsForUser(final UUID userUuid) {
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
                .subscribeOn(Schedulers.io());
    }

    public Single<Notepad> getNotepad(UUID notepadUuid, UUID userUuid) {
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
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteNotepad(UUID notepadUuid, UUID userUuid) {
        return Completable
                .fromAction(() -> {
                    Notepad notepad = notepadDao.fetchOneByUuid(notepadUuid);

                    if (notepad == null) {
                        throw new NotFoundException();
                    }

                    if (!notepad.getCreatorUserUuid().equals(userUuid)) {
                        throw new AuthorizationException("Cannot delete notepad you don't own");
                    }

                    notepadUserShareDao.delete(notepadUserShareDao.fetchByNotepadUuid(notepadUuid));
                    notepadDao.delete(notepad);
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Notepad> saveNotepad(Notepad notepad) {
        return Single
                .fromCallable(() -> {
                    notepadDao.update(notepad);

                    return notepad;
                })
                .subscribeOn(Schedulers.io());
    }

    public Completable shareNotepadWithUser(Notepad notepad, UUID shareWithUserUuid) {
        return Completable.fromAction(() -> {
            NotepadUserShare notepadUserShare = new NotepadUserShare();
            notepadUserShare.setNotepadUuid(notepad.getUuid());
            notepadUserShare.setUserUuid(shareWithUserUuid);
            notepadUserShare.setUuid(UUID.randomUUID());
            notepadUserShare.setDateCreated(Timestamp.from(timeProvider.now()));
            notepadUserShareDao.insert(notepadUserShare);
        });
    }
}
