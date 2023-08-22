package com.teams;

import com.teams.config.DBInit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Set;

/**
 * @author dgardi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class})
@EnableWebMvc
public class HotelManagementServiceApplication {

	public static void main(String[] args) {
		if(Arrays.asList(args).contains(DBInit.DB_INIT)){
			new SpringApplicationBuilder(DBInit.class)
					.profiles(DBInit.DB_INIT)
					.web(WebApplicationType.NONE)
					.run(args);
			return;
		}
		SpringApplication.run(HotelManagementServiceApplication.class, args);
	}

}
