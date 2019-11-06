package com.mick.vuetinaut.notepad;

import com.mick.vuetinaut.jooq.model.tables.daos.NotepadDao;
import com.mick.vuetinaut.jooq.model.tables.daos.NotepadUserShareDao;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.jooq.Configuration;

import javax.inject.Singleton;

@Factory
public class NotepadBeanFactory {
    @Bean
    @Singleton
    public NotepadDao notepadDao(final Configuration configuration) {
        return new NotepadDao(configuration);
    }

    @Bean
    @Singleton
    public NotepadUserShareDao notepadUserShareDao(final Configuration configuration) {
        return new NotepadUserShareDao(configuration);
    }
}
