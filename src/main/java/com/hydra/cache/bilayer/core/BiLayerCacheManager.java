package com.hydra.cache.bilayer.core;

import com.hydra.cache.bilayer.property.BiLayerCacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
public class BiLayerCacheManager implements CacheManager {

    Map<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private RedisTemplate<Object, Object> redisTemplate;
    private BiLayerCacheProperties dcConfig;

    public BiLayerCacheManager(RedisTemplate<Object, Object> redisTemplate,
                               BiLayerCacheProperties biLayerCacheProperties) {
        this.redisTemplate = redisTemplate;
        this.dcConfig = biLayerCacheProperties;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (Objects.nonNull(cache)) {
            return cache;
        }

        cache = new BiLayerCache(name, redisTemplate, createCaffeineCache(), dcConfig);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    private com.github.benmanes.caffeine.cache.Cache createCaffeineCache(){
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        Optional<BiLayerCacheProperties> dcConfigOpt = Optional.ofNullable(this.dcConfig);
        dcConfigOpt.map(BiLayerCacheProperties::getInit)
                .ifPresent(init->caffeineBuilder.initialCapacity(init));
        dcConfigOpt.map(BiLayerCacheProperties::getMax)
                .ifPresent(max->caffeineBuilder.maximumSize(max));
        dcConfigOpt.map(BiLayerCacheProperties::getExpireAfterWrite)
                .ifPresent(eaw->caffeineBuilder.expireAfterWrite(eaw,TimeUnit.SECONDS));
        dcConfigOpt.map(BiLayerCacheProperties::getExpireAfterAccess)
                .ifPresent(eaa->caffeineBuilder.expireAfterAccess(eaa,TimeUnit.SECONDS));
        dcConfigOpt.map(BiLayerCacheProperties::getRefreshAfterWrite)
                .ifPresent(raw->caffeineBuilder.refreshAfterWrite(raw,TimeUnit.SECONDS));
        return caffeineBuilder.build();
    }
}
