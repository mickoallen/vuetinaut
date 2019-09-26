package com.mick.vuetinaut.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;

@Factory
public class DatabaseFactory {

    @Context //lets connect on startup
    @Bean
    @Singleton
    public Configuration provideJooqConfiguration(DatabaseConfig databaseConfig) throws URISyntaxException {
        URI dbUri = new URI(databaseConfig.getUrl());

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        //add ssl if not localhost
        if(!"localhost".equals(dbUri.getHost())){
            dbUrl = dbUrl + "?sslmode=require";
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setMinimumIdle(databaseConfig.getMinPool());
        hikariConfig.setMaximumPoolSize(databaseConfig.getMaxPool());
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setConnectionTestQuery("SELECT 1;");
        hikariConfig.setConnectionTimeout(databaseConfig.getConnectionTimeout());

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return new DefaultConfiguration()
                .set(SQLDialect.POSTGRES)
                .set(hikariDataSource);
    }
}
