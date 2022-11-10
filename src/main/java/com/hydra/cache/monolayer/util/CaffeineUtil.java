package com.hydra.cache.monolayer.util;

import com.hydra.cache.common.util.SpringUtils;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * @author : Hydra
 * @date: 2022/10/20 11:29
 * @version: 1.0
 */
public class CaffeineUtil {

    public static Object get(String key){
        Cache<String,Object> cache = getCaffeine();
        return cache.getIfPresent(key);
    }

    public static void put(String key,Object value) {
        Cache<String,Object> cache = getCaffeine();
        cache.put(key, value);
    }

    private static Cache<String,Object> getCaffeine(){
        Cache<String,Object> cache = SpringUtils.getBean("caffeineCache");
        return cache;
    }

}
