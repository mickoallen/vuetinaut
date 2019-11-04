package com.mick.vuetinaut.exceptions;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.reactivex.Single;
import org.jooq.exception.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public static <T> Single<MutableHttpResponse<T>> handleError(Throwable error) {
        if (NotFoundException.class.isAssignableFrom(error.getClass()) ||
                NoDataFoundException.class.isAssignableFrom(error.getClass())) {
            return Single.just(HttpResponse.notFound());
        }

        if (BadRequestException.class.isAssignableFrom(error.getClass())) {
            return Single.just(HttpResponse.badRequest());
        }

        if(AuthorizationException.class.isAssignableFrom(error.getClass())){
            return Single.just(HttpResponseFactory.INSTANCE.status(HttpStatus.FORBIDDEN));
        }

        logger.error("Unexpected error", error);

        return Single.just(HttpResponse.serverError());
    }
}
