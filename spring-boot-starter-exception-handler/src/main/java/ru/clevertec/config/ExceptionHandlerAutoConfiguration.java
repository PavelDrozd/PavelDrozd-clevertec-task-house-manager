package ru.clevertec.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan("ru/clevertec/exception")
@ConditionalOnProperty(prefix = "spring.exception.handler", name = "enabled", matchIfMissing = true)
public class ExceptionHandlerAutoConfiguration {
}
