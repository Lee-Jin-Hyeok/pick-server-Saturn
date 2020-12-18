package com.dsm.pick.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    private final String driverClassName;
    private final String url;
    private final String username;
    private final String password;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    public DatabaseConfiguration(@Value("${DATABASE_DRIVER:com.mysql.cj.jdbc.Driver}")
                                         String driverClassName,
                                 @Value("${DATABASE_URL:jdbc:mysql://localhost:3306/pick?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC}")
                                         String url,
                                 @Value("${DATABASE_USERNAME:root}")
                                         String username,
                                 @Value("${DATABASE_PASSWORD:1111}")
                                         String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }
}