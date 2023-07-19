package com.teams.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.VaultBootstrapPropertySourceConfiguration;
import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration("VaultConfiguration")
@Slf4j
@AutoConfigureBefore(DBConfiguration.class)
public class VaultConfiguration {

    @Value("#{'${spring.cloud.vault.paths}'.split(',')}")
    private Set<String> valuesPaths;

    @Bean("vaultConfigurer")
    public VaultConfigurer vaultConfigurer() {
        return new VaultConfigurer() {
            @Override
            public void addSecretBackends(SecretBackendConfigurer configurer) {
                for (String paths : valuesPaths) {
                    configurer.add(paths);
                }
                configurer.registerDefaultDiscoveredSecretBackends(false);
                configurer.registerDefaultKeyValueSecretBackends(true);
            }
        };
    }

}
