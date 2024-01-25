package ru.clevertec.ecl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cache.Cache;
import ru.clevertec.ecl.data.response.HouseResponse;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class HouseCacheAspect {

    private final Cache<UUID, HouseResponse> cache;

    public HouseCacheAspect(@Qualifier("houseCache") Cache<UUID, HouseResponse> cache) {
        this.cache = cache;
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.HouseServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Get)")
    public void getMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.HouseServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Create)")
    public void createMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.HouseServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Update)")
    public void updateMethod() {
    }

    @Pointcut("execution(* ru.clevertec.ecl.service.impl.HouseServiceImpl.*(..)) " +
              "&& @annotation(ru.clevertec.ecl.aspect.annotation.Delete)")
    public void deleteMethod() {
    }

    @Around(value = "getMethod()")
    public Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: GET METHOD");

        UUID uuid = (UUID) joinPoint.getArgs()[0];

        Optional<HouseResponse> houseResponseInCache = cache.get(uuid);
        if (houseResponseInCache.isEmpty()) {
            HouseResponse houseResponse = (HouseResponse) joinPoint.proceed();

            cache.put(houseResponse.uuid(), houseResponse);

            return houseResponse;
        }

        return houseResponseInCache
                .orElseThrow();
    }

    @Around(value = "createMethod()")
    public Object createObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: CREATE METHOD");

        HouseResponse houseResponse = (HouseResponse) joinPoint.proceed();

        cache.put(houseResponse.uuid(), houseResponse);

        return houseResponse;
    }

    @Around(value = "updateMethod()")
    public Object updateObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("HOUSE CACHE ASPECT: UPDATE METHOD");

        HouseResponse houseResponse = (HouseResponse) joinPoint.proceed();

        cache.put(houseResponse.uuid(), houseResponse);

        return houseResponse;
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
