package ru.clevertec.ecl.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Profile;

@SpringBootConfiguration
@Profile("test")
public class DBContainerConfig {

    public static final String POSTGRES_CONTAINER_VERSION = "postgres:13.3";
}
