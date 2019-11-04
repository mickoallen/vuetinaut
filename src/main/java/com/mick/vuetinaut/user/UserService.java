package com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import com.mick.vuetinaut.security.PasswordService;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @Inject
    public UserService(
            final PasswordService passwordService,
            final UserRepository userRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
    }

    public Single<User> createUser(User user, String password) throws UsernameAlreadyExistsException {

        user.setUuid(UUID.randomUUID());
        user.setPassword(passwordService.getPasswordHash(password));
        user.setDateCreated(Timestamp.from(Instant.now()));

        return userRepository.fetchByUsername(user.getUsername())
                .doOnSuccess(existingUser -> {
                    logger.warn("Cannot create user. Username is already used");
                    throw new UsernameAlreadyExistsException("Cannot create user. Username is already used");
                })
                .onErrorResumeNext(throwable -> {
                    if (NotFoundException.class.isAssignableFrom(throwable.getClass())) {
                        return userRepository.insert(user);
                    }
                    return Single.error(throwable);
                });
    }

    public Single<User> getUserFromUuid(UUID userUuid) {
        return userRepository.fetchByUuid(userUuid);
    }

    public Single<User> getUserFromCredentials(String username, String password) throws NotFoundException {
        return userRepository.fetchFromCredentials(username, passwordService.getPasswordHash(password));
    }

    public Single<List<User>> searchByUsername(String username) {
        return userRepository.searchByUsername(username);
    }
}
