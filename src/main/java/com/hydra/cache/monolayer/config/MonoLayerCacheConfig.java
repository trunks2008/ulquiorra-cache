package com.hydra.cache.monolayer.config;

import com.hydra.cache.common.switcher.MonoSwitch;
import com.hydra.cache.monolayer.property.CaffeineProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author : Hydra
 * @date: 2022/10/19 10:09
 * @version: 1.0
 */
@EnableCaching
@Configuration
@ConditionalOnBean(MonoSwitch.Mono.class)
@EnableConfigurationProperties(CaffeineProperties.class)
public class MonoLayerCacheConfig {

    @Bean("caffeineCache")
    public Cache<String,Object> caffeineCache(CaffeineProperties caffeineProperties){
        return Caffeine.newBuilder()
                .initialCapacity(caffeineProperties.getInitialCapacity())//初始大小
                .maximumSize(caffeineProperties.getMaximumSize())//最大数量
                .expireAfterWrite(caffeineProperties.getExpireAfterWrite(), TimeUnit.SECONDS)//过期时间
                .build();
    }

    @Bean
    public CacheManager cacheManager(CaffeineProperties caffeineProperties){
        CaffeineCacheManager cacheManager=new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(caffeineProperties.getInitialCapacity())
                .maximumSize(caffeineProperties.getMaximumSize())
                .expireAfterWrite(caffeineProperties.getExpireAfterWrite(), TimeUnit.SECONDS));
        return cacheManager;
    }

}
