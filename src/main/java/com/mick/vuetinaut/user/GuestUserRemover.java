package com.mick.vuetinaut.user;

import io.micronaut.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Scheduled task to remove guest users.
 */
@Singleton
public class GuestUserRemover {

    private final UserService userService;

    @Inject
    public GuestUserRemover(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedDelay = "5m")
    public void removeExpiredGuestUsers() {
        userService.deleteExpiredGuestUsers().subscribe();
    }
}