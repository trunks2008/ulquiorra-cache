package com.hydra.cache.common.switcher;

import org.springframework.context.annotation.Bean;

/**
 * @author : Hydra
 * @date: 2022/10/21 13:48
 * @version: 1.0
 */
public class MonoSwitch {

    @Bean
    public Mono monoMarker(){
        return new Mono();
    }

    public class Mono {
    }

}
