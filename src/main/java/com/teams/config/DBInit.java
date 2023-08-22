package com.teams.config;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author dgardi
 */

@Import({DBConfiguration.class, LiquibaseAutoConfiguration.class, VaultConfiguration.class})
public class DBInit {
    public static final String DB_INIT = "dbinit";
}
