package com.davivienda.sv.app.configuration.sqlserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.sqlserver.hikari")
public class HikariProperties {
    private String poolName;
    private int maximumPoolSize = 10;
    private long connectionTimeout = 30000;
    private long idleTimeout = 600000;
    private long maxLifetime = 1800000;
    private int minimumIdle = 5;
}