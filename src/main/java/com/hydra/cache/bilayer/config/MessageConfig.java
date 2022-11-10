package com.hydra.cache.bilayer.config;

import com.hydra.cache.bilayer.core.BiLayerCacheManager;
import com.hydra.cache.bilayer.msg.RedisMessageReceiver;
import com.hydra.cache.common.switcher.BiSwitch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
@Configuration
@ConditionalOnBean(BiSwitch.Bi.class)
public class MessageConfig {
    public static final String TOPIC="cache.msg";

    @Bean
    RedisMessageReceiver redisMessageReceiver(@Qualifier(value = "biRedisTemplate") RedisTemplate<Object, Object> redisTemplate,
                                              BiLayerCacheManager biLayerCacheManager){
        return new RedisMessageReceiver(redisTemplate, biLayerCacheManager);
    }

    @Bean
    MessageListenerAdapter adapter(RedisMessageReceiver receiver){
        return new MessageListenerAdapter(receiver,"receive");
    }

    @Bean
//    @ConditionalOnBean(RedisConnectionFactory.class)
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter,
                                            RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(TOPIC));
        return container;
    }

}
