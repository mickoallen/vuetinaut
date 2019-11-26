package com.mick.vuetinaut.security;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.handlers.LoginHandler;
import io.micronaut.security.token.jwt.cookie.JwtCookieConfiguration;
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator;
import io.micronaut.security.token.jwt.generator.JwtGeneratorConfiguration;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;

import javax.inject.Singleton;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

/**
 * Need to override the default Micronaut login handler as it does a redirect on login, oauth standards aside, i'd rather the front end app handle that.
 */
@Requires(
        property = "micronaut.security.token.jwt.cookie.redirect.enabled",
        value = "false"
)
@Singleton
@Primary /* need to set it as the primary bean till my pr is merged - https://github.com/micronaut-projects/micronaut-security/pull/97 */
public class JwtCookieNonRedirectingLoginHandler implements LoginHandler {
    protected final JwtCookieConfiguration jwtCookieConfiguration;
    protected final AccessRefreshTokenGenerator accessRefreshTokenGenerator;
    protected final JwtGeneratorConfiguration jwtGeneratorConfiguration;

    public JwtCookieNonRedirectingLoginHandler(JwtCookieConfiguration jwtCookieConfiguration, JwtGeneratorConfiguration jwtGeneratorConfiguration, AccessRefreshTokenGenerator accessRefreshTokenGenerator) {
        this.jwtCookieConfiguration = jwtCookieConfiguration;
        this.jwtGeneratorConfiguration = jwtGeneratorConfiguration;
        this.accessRefreshTokenGenerator = accessRefreshTokenGenerator;
    }

    public HttpResponse loginSuccess(UserDetails userDetails, HttpRequest<?> request) {
        Optional<AccessRefreshToken> accessRefreshTokenOptional = this.accessRefreshTokenGenerator.generate(userDetails);
        if (accessRefreshTokenOptional.isPresent()) {
            Cookie cookie = Cookie.of(this.jwtCookieConfiguration.getCookieName(), ((AccessRefreshToken) accessRefreshTokenOptional.get()).getAccessToken());
            cookie.configure(this.jwtCookieConfiguration, request.isSecure());
            Optional<TemporalAmount> cookieMaxAge = this.jwtCookieConfiguration.getCookieMaxAge();
            if (cookieMaxAge.isPresent()) {
                cookie.maxAge((TemporalAmount) cookieMaxAge.get());
            } else {
                cookie.maxAge((long) this.jwtGeneratorConfiguration.getAccessTokenExpiration());
            }

            return HttpResponse.ok().cookie(cookie);
        } else {
            return HttpResponse.serverError();
        }
    }

    public HttpResponse loginFailed(AuthenticationFailed authenticationFailed) {
        return HttpResponse.unauthorized();
    }
}
