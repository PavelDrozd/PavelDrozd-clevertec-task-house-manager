package ru.clevertec.ecl.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DBConfig {

    private static final String ENTITY_FOLDER = "ru.clevertec.ecl.entity";
    private static final String HIBERNATE_TRANSACTION_JTA_PLATFORM = "hibernate.transaction.jta.platform";
    private static final String HIBERNATE_OGM_DATASTORE_PROVIDER = "hibernate.ogm.datastore.provider";
    private static final String HIBERNATE_USE_SQL_COMMENTS = "hibernate.use_sql_comments";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

    @Value("${hibernate.transaction.jta.platform}")
    private String HIBERNATE_TRANSACTION_JTA_PLATFORM_VALUE;
    @Value("${hibernate.ogm.datastore.provider}")
    private String HIBERNATE_OGM_DATASTORE_PROVIDER_VALUE;
    @Value("${hibernate.use_sql_comments}")
    private String HIBERNATE_USE_SQL_COMMENTS_VALUE;
    @Value("${hibernate.show_sql}")
    private String HIBERNATE_SHOW_SQL_VALUE;
    @Value("${hibernate.format_sql}")
    private String HIBERNATE_FORMAT_SQL_VALUE;

    @Value("${database.driver}")
    private String DB_DRIVER;
    @Value("${database.url}")
    private String DB_URL;
    @Value("${database.user}")
    private String DB_USER;
    @Value("${database.password}")
    private String DB_PASSWORD;


    @Bean
    public EntityManager entityManager() {
        try (EntityManager entityManager = localContainerEntityManagerFactoryBean().getNativeEntityManagerFactory().createEntityManager()) {
            return entityManager;
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(ENTITY_FOLDER);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties properties = getHibernateProperties();
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(DB_DRIVER);
        hikariConfig.setDriverClassName(DB_DRIVER);
        hikariConfig.setJdbcUrl(DB_URL);
        hikariConfig.setUsername(DB_USER);
        hikariConfig.setPassword(DB_PASSWORD);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/v0.0/db.changelog-master.yml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();

        properties.setProperty(HIBERNATE_TRANSACTION_JTA_PLATFORM, HIBERNATE_TRANSACTION_JTA_PLATFORM_VALUE);
        properties.setProperty(HIBERNATE_OGM_DATASTORE_PROVIDER, HIBERNATE_OGM_DATASTORE_PROVIDER_VALUE);
        properties.setProperty(HIBERNATE_USE_SQL_COMMENTS, HIBERNATE_USE_SQL_COMMENTS_VALUE);
        properties.setProperty(HIBERNATE_SHOW_SQL, HIBERNATE_SHOW_SQL_VALUE);
        properties.setProperty(HIBERNATE_FORMAT_SQL, HIBERNATE_FORMAT_SQL_VALUE);

        return properties;
    }
}

