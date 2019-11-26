package com.mick.vuetinaut.security;

import io.micronaut.security.authentication.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.UUID;

/**
 * Helper class for dealing with the {@link java.security.Principal}
 */
public final class PrincipalUtils {
    private static final Logger logger = LoggerFactory.getLogger(PrincipalUtils.class);

    /**
     * @param principal to get the {@link UUID} for.
     * @return {@link UUID} of the given principal
     * @throws AuthenticationException if the {@link UUID} is missing from the principal, or invalid.
     */
    public static UUID getUserUuid(final Principal principal) {
        String name = principal.getName();

        if (name == null || name.isEmpty()) {
            throw new AuthenticationException("Unable to determine user from principal");
        }

        try {
            return UUID.fromString(name);
        } catch (Exception e) {
            logger.error("Unable to convert principal name to uuid: {}", name, e);
            throw new AuthenticationException("Unable to determine user from principal");
        }
    }
}
