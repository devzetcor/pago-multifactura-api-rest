package com.davivienda.sv.app.configuration.sqlserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.sqlserver.datasource")
public class SQLServerProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
}
