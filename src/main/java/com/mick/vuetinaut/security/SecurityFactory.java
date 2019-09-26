package com.mick.vuetinaut.security;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Named;

@Factory
public class SecurityFactory {

    @Bean
    @Named("password.salt")
    public String getSalt() {
        return "your mom";
    }
}
