package com.teams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Set;

/**
 * @author dgardi
 */
@Configuration("VaultConfiguration")
@Order(1)
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
                configurer.registerDefaultDiscoveredSecretBackends(true);
                configurer.registerDefaultDiscoveredSecretBackends(false);
            }
        };
    }

}
