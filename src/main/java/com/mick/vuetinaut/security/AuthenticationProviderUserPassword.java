package com.mick.vuetinaut.security;

import com.mick.vuetinaut.user.UserService;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Authenticate via username password.
 */
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

    private final UserService userService;

    @Inject
    public AuthenticationProviderUserPassword(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if (!UsernamePasswordCredentials.class.isAssignableFrom(authenticationRequest.getClass())) {
            throw new AuthenticationException("Authentication only available through username/password");
        }

        return getUserDetailsFromAuthenticationRequest((UsernamePasswordCredentials) authenticationRequest)
                .doOnError(exception -> logger.error("Exception getting user from username and password", exception))
                .onErrorReturn(exception -> new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
    }

    /**
     * Move this into a method to trick java (really I blame RX) to allow returning of different implementations of {@link AuthenticationResponse}.
     *
     * @param authenticationRequest
     * @return
     */
    private Flowable<AuthenticationResponse> getUserDetailsFromAuthenticationRequest(UsernamePasswordCredentials authenticationRequest) {
        return userService
                .getUserFromCredentials(
                        authenticationRequest.getIdentity(),
                        authenticationRequest.getSecret()
                ).toFlowable()
                .map(user -> new UserDetails(user.getUuid().toString(), List.of()));
    }
}
