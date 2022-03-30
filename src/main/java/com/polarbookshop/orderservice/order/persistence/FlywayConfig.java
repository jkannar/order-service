package com.polarbookshop.orderservice.order.persistence;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

// This is not needed for simple RDBC, but it seems that R2DBC doesn't
// work without this class...
@Configuration
public class FlywayConfig {

    private final Environment env;

    public FlywayConfig(final Environment env) {
        this.env = env;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(
                        env.getRequiredProperty("spring.flyway.url"),
                        env.getRequiredProperty("spring.flyway.user"),
                        env.getRequiredProperty("spring.flyway.password"))
        );
    }
}
