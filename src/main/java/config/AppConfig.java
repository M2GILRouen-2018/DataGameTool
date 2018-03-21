package config;

import controller.InitController;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "io.univ.rouen.m2gil.smartclass.core",
        "service"
})
@EntityScan(basePackages = {
        "io.univ.rouen.m2gil.smartclass.core"
})
@EnableJpaRepositories(basePackages = {
        "io.univ.rouen.m2gil.smartclass.core"
})
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public InitController initController() {
        return new InitController();
    }
}
