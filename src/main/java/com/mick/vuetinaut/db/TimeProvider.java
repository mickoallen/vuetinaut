package com.mick.vuetinaut.db;

import javax.inject.Singleton;
import java.time.Instant;

/**
 * This class should be used anytime 'now' is needed in code.
 * This makes mocking & testing easier.
 */
@Singleton
public class TimeProvider {
    public Instant now(){
        return Instant.now();
    }
}
