package config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {
        "io.univ.rouen.m2gil.smartclass"
})
@EnableJpaRepositories(basePackages = {
        "io.univ.rouen.m2gil.smartclass"
})
@EnableTransactionManagement
public class AppConfig {}
