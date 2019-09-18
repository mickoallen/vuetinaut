package com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.daos.UserDao;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import com.mick.vuetinaut.security.PasswordService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.field;

@Singleton
public class UserService {
    private final PasswordService passwordService;
    private final UserDao userDao;

    @Inject
    public UserService(
            final PasswordService passwordService,
            final UserDao userDao){
        this.passwordService = passwordService;
        this.userDao = userDao;
    }

    public User createUser(User user) throws UsernameAlreadyExistsException {
        List<User> users = userDao.fetchByUsername(user.getUsername());
        if (!users.isEmpty()) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " is already taken.");
        }

        user.setUuid(UUID.randomUUID());
        user.setPassword(passwordService.getPasswordHash(user.getPassword()));
        user.setDateCreated(Timestamp.from(Instant.now()));
        userDao.insert(user);
        return user;
    }

    public User getUserFromCredentials(String username, String password) throws NotFoundException {
        return userDao
                .configuration()
                .dsl()
                .selectFrom(userDao.getTable())
                .where(field(com.mick.vuetinaut.jooq.model.tables.User.USER.USERNAME)
                        .eq(username))
                .and(field(com.mick.vuetinaut.jooq.model.tables.User.USER.PASSWORD)
                        .eq(passwordService.getPasswordHash(password)))
                .fetchOptionalInto(User.class)
                .orElseThrow(() -> new NotFoundException("No user for that username/password"));
    }

    public User getUser(UUID uuid) throws NotFoundException {
        User user = userDao
                .fetchOneByUuid(uuid);

        if (user == null) {
            throw new NotFoundException("No user for that uuid");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User updateUser(User user) {
        userDao.update(user);
        return user;
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }
}
