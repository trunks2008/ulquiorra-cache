package com.hydra.cache.bilayer.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "bilayer")
public class BiLayerCacheProperties {

    private Boolean allowNull = true;

    private Integer init = 100;

    private Integer max = 1000;

    private Long expireAfterWrite ;

    private Long expireAfterAccess;

    private Long refreshAfterWrite;

    private Long redisExpire;

}
