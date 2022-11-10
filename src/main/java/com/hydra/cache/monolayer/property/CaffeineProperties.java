package com.hydra.cache.monolayer.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:38
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix="caffeine")
public class CaffeineProperties {

    private Integer initialCapacity;

    private Long maximumSize;

    private Long expireAfterWrite;

}
