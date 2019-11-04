package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.db.TimeProvider;
import com.mick.vuetinaut.exceptions.AuthorizationException;
import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.daos.NoteDao;
import com.mick.vuetinaut.jooq.model.tables.daos.NotepadDao;
import com.mick.vuetinaut.jooq.model.tables.daos.NotepadUserShareDao;
import com.mick.vuetinaut.jooq.model.tables.pojos.Note;
import com.mick.vuetinaut.jooq.model.tables.pojos.Notepad;
import com.mick.vuetinaut.jooq.model.tables.pojos.NotepadUserShare;
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
import java.util.stream.Collectors;

@Singleton
public class NotepadRepository {
    private static final Logger logger = LoggerFactory.getLogger(NotepadRepository.class);

    private final NoteDao noteDao;
    private final NotepadDao notepadDao;
    private final NotepadUserShareDao notepadUserShareDao;
    private final TimeProvider timeProvider;

    @Inject
    public NotepadRepository(
            final NoteDao noteDao,
            final NotepadDao notepadDao,
            final NotepadUserShareDao notepadUserShareDao,
            final TimeProvider timeProvider) {
        this.noteDao = noteDao;
        this.notepadDao = notepadDao;
        this.notepadUserShareDao = notepadUserShareDao;
        this.timeProvider = timeProvider;
    }

    public Completable create(Notepad notepad) {
        return Completable.fromAction(() -> notepadDao.insert(notepad));
    }

    public Single<List<Notepad>> getNotepadsForUser(final UUID userUuid) {
        //todo - optimize
        return Single
                .fromCallable(() -> {
                    List<NotepadUserShare> notepadUserShares = notepadUserShareDao.fetchByUserUuid(userUuid);
                    List<Notepad> userSharedNotepads = notepadDao.fetchByUuid(
                            notepadUserShares
                                    .stream()
                                    .map(NotepadUserShare::getUuid)
                                    .toArray(UUID[]::new)
                    );
                    List<Notepad> usersNotepads = notepadDao.fetchByCreatorUserUuid(userUuid);

                    usersNotepads.addAll(userSharedNotepads);
                    return usersNotepads;
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Notepad> getNotepad(UUID notepadUuid) {
        //todo optimize
        return Single
                .fromCallable(() -> {
                    Notepad notepad = notepadDao.fetchOneByUuid(notepadUuid);

                    if (notepad == null) {
                        throw new NotFoundException("Notepad doesn't exist");
                    }

                    return notepad;
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<NotepadLoaded> loadNotepad(Notepad notepad, UUID userUuid) {
        return Single
                .fromCallable(() -> {
                    if (!notepad.getCreatorUserUuid().equals(userUuid)) {
                        if (notepadUserShareDao.fetchByUserUuid(userUuid)
                                .stream()
                                .noneMatch(notepadUserShare -> notepadUserShare.getNotepadUuid().equals(notepad.getUuid()))) {
                            throw new AuthorizationException("User not authorized to view that notepad");
                        }
                    }

                    return new NotepadLoaded()
                            .setNotepad(notepad)
                            .setNotes(noteDao.fetchByNotepadUuid(notepad.getUuid()));
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<List<NotepadLoaded>> loadNotepadsWithNotes(List<Notepad> notepads) {
        //todo optimize
        return Single
                .fromCallable(() -> {
                    List<Note> notes = noteDao.fetchByNotepadUuid(notepads.stream().map(Notepad::getUuid).toArray(UUID[]::new));

                    return notepads
                            .stream()
                            .map(notepad -> new NotepadLoaded().setNotepad(notepad))
                            .map(notepadLoaded -> notepadLoaded.setNotes(
                                    notes
                                            .stream()
                                            .filter(note -> note.getNotepadUuid().equals(notepadLoaded.getNotepad().getUuid()))
                                            .collect(Collectors.toList())
                            )).collect(Collectors.toList());

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

                    //todo - optimize & transaction
                    noteDao.delete(noteDao.fetchByNotepadUuid(notepadUuid));
                    notepadUserShareDao.delete(notepadUserShareDao.fetchByNotepadUuid(notepadUuid));
                    notepadDao.delete(notepad);
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Note> create(Note note) {
        return Single
                .fromCallable(() -> {
                    noteDao.insert(note);
                    return note;
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Note> getNote(UUID uuid) {
        return Single
                .fromCallable(() -> {
                    Note note = noteDao.fetchOneByUuid(uuid);

                    if (note == null) {
                        throw new NotFoundException();
                    }

                    return note;
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Note> saveNote(Note editedNote) {
        return Single
                .fromCallable(() -> {
                    noteDao.update(editedNote);

                    return editedNote;
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

    public Completable deleteNote(UUID notepadUuid, UUID userUuid) {
return null;
    }
}
