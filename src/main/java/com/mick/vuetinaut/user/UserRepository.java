package com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.daos.UserDao;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.field;

@Singleton
public class UserRepository {
    private final UserDao userDao;

    @Inject
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public Single<User> insert(User user) {
        return Single.fromCallable(() -> {
            userDao.insert(user);
            return user;
        });
    }

    public Single<User> fetchByUsername(String username) {
        return Single
                .fromCallable(() ->
                        userDao.fetchByUsername(username)
                                .stream()
                                .findFirst()
                                .orElseThrow(NotFoundException::new)
                )
                .subscribeOn(Schedulers.io());
    }

    public Single<User> fetchByUuid(UUID uuid) {
        return Single
                .fromCallable(() -> {
                            User user = userDao.fetchOneByUuid(uuid);
                            if (user == null) {

                                throw new NotFoundException("No user for that UUID");
                            }
                            return user;
                        }
                )
                .subscribeOn(Schedulers.io());
    }

    public Single<User> fetchFromCredentials(String username, String hashedPassword) {
        return Single
                .fromCallable(() -> userDao
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
                .subscribeOn(Schedulers.io());
    }

    /**
     * Poor mans elasticsearch.
     *
     * @param username
     * @return
     */
    public Single<List<User>> searchByUsername(String username) {
        return Single
                .fromCallable(() ->
                        userDao
                                .configuration()
                                .dsl()
                                .selectFrom(userDao.getTable())
                                .where(
                                        field(com.mick.vuetinaut.jooq.model.tables.User.USER.USERNAME)
                                                .likeIgnoreCase("%" + username + "%")
                                )
                                .fetchInto(User.class)
                )
                .subscribeOn(Schedulers.io());
    }
}
