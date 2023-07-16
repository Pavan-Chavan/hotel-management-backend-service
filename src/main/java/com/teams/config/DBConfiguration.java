package com.teams.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

import static com.teams.constant.HoteManagementConstants.*;

@Configuration
public class DBConfiguration {
    @Value("${oracle.driver.class}")
    private String driverClass;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${oracle.database.username}")
    private String username;
    @Value("${oracle.database.password}")
    private String password;

    @Bean
    public DataSource dataSource(){
        Properties properties = new Properties();
        properties.setProperty(DRIVER_CLASS_NAME, driverClass);
        properties.setProperty(JDBC_URL, jdbcUrl);
        properties.setProperty(USERNAME, username);
        properties.setProperty(PASSWORD, password);
        HikariConfig hikariConfig = new HikariConfig(properties);
        return new HikariDataSource(hikariConfig);

    }
}
