package com.mick.vuetinaut.db;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("db")
public class DatabaseConfig {

    private String url;
    private int minPool;
    private int maxPool;
    private int connectionTimeout;

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
