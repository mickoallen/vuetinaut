package com.mick.vuetinaut.db;

import javax.inject.Singleton;
import java.time.Instant;

@Singleton
public class TimeProvider {
    public Instant now(){
        return Instant.now();
    }
}
