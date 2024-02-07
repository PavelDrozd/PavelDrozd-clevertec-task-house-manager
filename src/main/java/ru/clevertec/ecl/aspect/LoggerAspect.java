package ru.clevertec.ecl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("execution(public * (@ru.clevertec.ecl.aspect.annotation.Logger *).*(..))")
    public void loggingPublicMethodsOfClass() {
    }

    @Before("loggingPublicMethodsOfClass()")
    public void beforeLoggingPublicMethodsOfClass(JoinPoint joinPoint) {
        log.info("INVOKE METHOD: " + joinPoint.getSignature()
                 + " WITH ARGS: " + Arrays.toString(joinPoint.getArgs()));
    }
}
