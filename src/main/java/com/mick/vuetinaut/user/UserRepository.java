package com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.daos.UserDao;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.field;

/**
 * Data access layer for users.
 */
@Singleton
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final int USER_SEARCH_LIMIT = 100; //this should probably move up to the rest layer and be a query param - TODO
    private final UserDao userDao;

    @Inject
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Create the given {@link User}.
     * <p>
     * *this pattern is different to notes since it's return a {@link Single}.
     * This just made it easier to handle username matching.
     * Might clean it up one day.
     *
     * @param user
     * @return
     */
    public Single<User> create(User user) {
        logger.trace("Creating user: {}", user);
        return Single
                .fromCallable(() -> {
                    try {
                        userDao.insert(user);
                    } catch (DataAccessException e) {
                        logger.error("Failed to create user: {}", user, e);
                        throw e;
                    }
                    return user;
                })
                .doOnSuccess(createdUser -> logger.trace("Created user with uuid: {}", createdUser.getUuid()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * note: Usernames are enforced to be unique.
     *
     * @param username username to fetch by.
     * @return {@link User} with the given username.
     * @throws NotFoundException if the user doesn't exist.
     */
    public Single<User> getByUsername(String username) {
        logger.trace("Getting user by username: {}", username);
        return Single
                .fromCallable(() ->
                        userDao.fetchByUsername(username)
                                .stream()
                                .findFirst()
                                .orElseThrow(NotFoundException::new)
                )
                .doOnSuccess(user -> logger.trace("Found user with uuid: {}", user.getUuid()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * @param uuid {@link UUID} to fetch user by.
     * @return {@link User} with the given {@link UUID}.
     * @throws NotFoundException if no user exists for that {@link UUID}.
     */
    public Single<User> getByUuid(UUID uuid) {
        logger.trace("Getting user by uuid: {}", uuid);
        return Single
                .fromCallable(() -> {
                            User user = userDao.fetchOneByUuid(uuid);
                            if (user == null) {
                                throw new NotFoundException("No user for that UUID");
                            }
                            return user;
                        }
                )
                .doOnSuccess(user -> logger.trace("Found user with uuid: {}", uuid))
                .subscribeOn(Schedulers.io());
    }

    /**
     * @param username       String username.
     * @param hashedPassword String hashed password.
     * @return {@link User} for the given credentials.
     * @throws NotFoundException if no user exists for that username/password
     */
    public Single<User> fetchFromCredentials(String username, String hashedPassword) {
        logger.trace("Fetching user with username: {} and password", username);
        return Single
                .fromCallable(() ->
                        userDao
                                .configuration()
                                .dsl()
                                .selectFrom(userDao.getTable())
                                .where(field(com.mick.vuetinaut.jooq.model.tables.User.USER.USERNAME)
                                        .eq(username))
                                .and(field(com.mick.vuetinaut.jooq.model.tables.User.USER.PASSWORD)
                                        .eq(hashedPassword))
                                .fetchOptionalInto(User.class)
                                .orElseThrow(NotFoundException::new)
                )
                .doOnSuccess(user -> logger.trace("Found user with uuid: {}", user.getUuid()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Poor mans searching.
     *
     * @param usernameContains Search for users whos username contains this string.
     * @return List of {@link User} users matching.
     */
    public Single<List<User>> searchByUsernameContains(String usernameContains) {
        logger.trace("Seaching for user whos username contains: {}", usernameContains);
        return Single
                .fromCallable(() ->
                        userDao
                                .configuration()
                                .dsl()
                                .selectFrom(userDao.getTable())
                                .where(
                                        field(com.mick.vuetinaut.jooq.model.tables.User.USER.USERNAME)
                                                .likeIgnoreCase("%" + usernameContains + "%")
                                )
                                .limit(USER_SEARCH_LIMIT)
                                .fetchInto(User.class)
                )
                .doOnSuccess(users -> {
                    //janky logging cos i dont want hashed passwords being logged
                    if (logger.isTraceEnabled()) {
                        logger.trace("Found users: [");
                        users.forEach(user -> logger.trace("uuid:{}", user.getUuid()));
                        logger.trace("]");
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
