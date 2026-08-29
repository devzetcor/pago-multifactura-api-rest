package com.davivienda.sv.app.configuration.sqlserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.sqlserver.datasource")
public class SQLServerProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
}
