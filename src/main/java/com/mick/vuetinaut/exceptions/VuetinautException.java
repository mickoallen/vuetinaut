package com.mick.vuetinaut.exceptions;

public class VuetinautException extends RuntimeException {
    public VuetinautException() {
    }

    public VuetinautException(String message) {
        super(message);
    }

    public VuetinautException(String message, Throwable cause) {
        super(message, cause);
    }

    public VuetinautException(Throwable cause) {
        super(cause);
    }

    public VuetinautException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
