package ru.clevertec.ecl.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/v0.0/db.changelog-master.yml");
        liquibase.setDataSource(dataSource);
        liquibase.setDropFirst(true);
        return liquibase;
    }
}
