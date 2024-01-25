package ru.clevertec.ecl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.cache.impl.LFUCacheImpl;
import ru.clevertec.ecl.cache.Cache;
import ru.clevertec.ecl.cache.impl.LRUCacheImpl;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.exception.ConfigurationException;

import java.util.UUID;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<UUID, HouseResponse> houseCache(@Value("${cache.size}") int initialCapacity,
                                                 @Value("${cache.type}") String cacheType) {
        if (cacheType.equals("LRU")) {
            return new LRUCacheImpl<>(initialCapacity);
        } else if (cacheType.equals("LFU")) {
            return new LFUCacheImpl<>(initialCapacity);
        } else {
            throw ConfigurationException.of(Cache.class, cacheType, initialCapacity);
        }
    }

    @Bean
    public Cache<UUID, PersonResponse> personCache(@Value("${cache.size}") int initialCapacity,
                                                   @Value("${cache.type}") String cacheType) {
        if (cacheType.equals("LRU")) {
            return new LRUCacheImpl<>(initialCapacity);
        } else if (cacheType.equals("LFU")) {
            return new LFUCacheImpl<>(initialCapacity);
        } else {
            throw ConfigurationException.of(Cache.class, cacheType, initialCapacity);
        }
    }
}
