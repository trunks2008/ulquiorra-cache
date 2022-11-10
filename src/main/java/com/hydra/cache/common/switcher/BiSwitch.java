package com.hydra.cache.common.switcher;

import org.springframework.context.annotation.Bean;

/**
 * @author : Hydra
 * @date: 2022/10/24 10:31
 * @version: 1.0
 */
public class BiSwitch {

    @Bean
    public Bi BiMarker(){
        return new Bi();
    }

    public class Bi {
    }
}
