package com.mick.vuetinaut.user;

import com.mick.vuetinaut.jooq.model.tables.daos.UserDao;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.jooq.Configuration;

import javax.inject.Singleton;

@Factory
public class UserBeanFactory {
    @Bean
    @Singleton
    public UserDao userDao(final Configuration configuration) {
        return new UserDao(configuration);
    }
}
