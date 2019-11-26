package com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import com.mick.vuetinaut.notepad.NoteService;
import com.mick.vuetinaut.security.PasswordService;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Business logic for users.
 */
@Singleton
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final NoteService noteService;

    @Inject
    public UserService(
            final PasswordService passwordService,
            final UserRepository userRepository,
            final NoteService noteService) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.noteService = noteService;
    }

    /**
     * @param user     {@link} User to create.
     * @param password Raw password for the user.
     * @return The created {@link User}.
     * @throws UsernameAlreadyExistsException if the username is already taken
     */
    public Single<User> createUser(User user, String password) throws UsernameAlreadyExistsException {

        user.setUuid(UUID.randomUUID());
        user.setPassword(passwordService.getPasswordHash(password));
        user.setDateCreated(Timestamp.from(Instant.now()));

        return userRepository.getByUsername(user.getUsername())
                .doOnSuccess(existingUser -> {
                    logger.warn("Cannot create user. Username is already used");
                    throw new UsernameAlreadyExistsException("Cannot create user. Username is already used");
                })
                .onErrorResumeNext(throwable -> {
                    if (NotFoundException.class.isAssignableFrom(throwable.getClass())) {
                        return userRepository.create(user);
                    }
                    return Single.error(throwable);
                });
    }

    /**
     * @param userUuid {@link UUID} to fetch the user by.
     * @return {@link User} with the given uuid.
     */
    public Single<User> getUserFromUuid(UUID userUuid) {
        return userRepository.getByUuid(userUuid);
    }

    /**
     * @param username Username of the {@link User}.
     * @param password Password of the {@link User}.
     * @return {@link User} matching the credentials
     */
    public Single<User> getUserFromCredentials(String username, String password) {
        return userRepository.fetchFromCredentials(username, passwordService.getPasswordHash(password));
    }

    /**
     * @param usernameContains Search for users whos username contains this.
     * @return List of {@link User}
     */
    public Single<List<User>> searchByUsernameContains(String usernameContains) {
        return userRepository.searchByUsernameContains(usernameContains);
    }

    /**
     * @param user Delete the given {@link User}.
     */
    public Completable deleteUser(User user){
        return userRepository.deleteUser(user);
    }

    /**
     * Delete the expired guest users.
     * its functions like this that me really dislike rxjava, or love it?
     */
    public Completable deleteExpiredGuestUsers(){
        return userRepository
                .getExpiredGuestUsers()
                .flatMapSingle(user ->
                        deleteUser(user)
                                .andThen(Single.just(user)) // pass the user on
                )
                .flatMapCompletable(noteService::deleteNotesForUser);
    }
}
