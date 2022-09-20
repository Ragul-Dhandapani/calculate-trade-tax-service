package com.morganstanley.calculatetradetaxservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import static com.morganstanley.calculatetradetaxservice.constants.ApplicationConstants.CACHE_NAME;

@Configuration
public class CacheConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfiguration.class);

    @Autowired
    CacheManager cacheManager;

    public void refreshAllCaches() {
        LOGGER.info("cache clear is started");
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(CACHE_NAME).clear());
    }

    @Scheduled(fixedRate = 10000) //10 seconds
    public void refreshAllCachesAtIntervals() {
        refreshAllCaches();
    }
}
