package com.hydra.cache.common.config;

import com.hydra.cache.common.util.MinimalismKeyGenerator;
import com.hydra.cache.common.util.SpringUtils;
import org.springframework.context.annotation.Bean;

/**
 * @author : Hydra
 * @date: 2022/10/24 11:14
 * @version: 1.0
 */
public class CommonConfig {

    @Bean("caffeineSpringUtil")
    public SpringUtils springUtils(){
        return new SpringUtils();
    }

    @Bean
    public MinimalismKeyGenerator minimalismKeyGenerator(){
        return new MinimalismKeyGenerator();
    }

}
