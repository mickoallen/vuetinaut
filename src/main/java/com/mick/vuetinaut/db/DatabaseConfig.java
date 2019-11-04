package com.mick.vuetinaut.db;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;

@ConfigurationProperties("db")
public class DatabaseConfig {

    @Property(name = "url")
    private String url;
    @Property(name = "min-pool")
    private int minPool;
    @Property(name = "max-pool")
    private int maxPool;
    @Property(name = "connection-timeout")
    private int connectionTimeout;


    public DatabaseConfig() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMinPool() {
        return minPool;
    }

    public void setMinPool(int minPool) {
        this.minPool = minPool;
    }

    public int getMaxPool() {
        return maxPool;
    }

    public void setMaxPool(int maxPool) {
        this.maxPool = maxPool;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
