package ru.clevertec.ecl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cache.Cache;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class PersonCacheAspect {

    private final Cache<UUID, PersonResponse> cache;

    public PersonCacheAspect(@Qualifier("personCache") Cache<UUID, PersonResponse> cache) {
        this.cache = cache;
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.PersonServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Get)")
    public void getMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.PersonServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Create)")
    public void createMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.PersonServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Update)")
    public void updateMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.PersonServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Delete)")
    public void deleteMethod() {
    }

    @Around(value = "getMethod()")
    public Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: GET METHOD");

        UUID uuid = (UUID) joinPoint.getArgs()[0];

        Optional<PersonResponse> personResponseInCache = cache.get(uuid);
        if (personResponseInCache.isEmpty()) {
            PersonResponse personResponse = (PersonResponse) joinPoint.proceed();

            cache.put(personResponse.uuid(), personResponse);

            return personResponse;
        }

        return personResponseInCache
                .orElseThrow();
    }

    @Around(value = "createMethod()")
    public Object createObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: CREATE METHOD");

        PersonResponse personResponse = (PersonResponse) joinPoint.proceed();

        cache.put(personResponse.uuid(), personResponse);

        return personResponse;
    }

    @Around(value = "updateMethod()")
    public Object updateObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: UPDATE METHOD");

        PersonResponse personResponse = (PersonResponse) joinPoint.proceed();

        cache.put(personResponse.uuid(), personResponse);

        return personResponse;
    }

    @Around(value = "deleteMethod()")
    public Object deleteObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: DELETE METHOD");

        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Object proceed = joinPoint.proceed();

        cache.remove(uuid);

        return proceed;
    }
}
