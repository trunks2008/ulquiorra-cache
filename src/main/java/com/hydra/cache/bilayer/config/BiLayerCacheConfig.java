package com.hydra.cache.bilayer.config;

import com.hydra.cache.bilayer.core.BiLayerCacheManager;
import com.hydra.cache.bilayer.property.BiLayerCacheProperties;
import com.hydra.cache.common.switcher.BiSwitch;
import com.hydra.cache.common.util.JacksonComponent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
@EnableCaching
@Configuration
@ConditionalOnBean(BiSwitch.Bi.class)
@EnableConfigurationProperties(BiLayerCacheProperties.class)
public class BiLayerCacheConfig extends CachingConfigurerSupport {

    @Bean("biRedisTemplate")
    @ConditionalOnClass(value = RedisTemplate.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // json序列化设置
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om=getObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //String类型的序列化
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);// key采用String序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key采用String序列化方式
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public BiLayerCacheManager cacheManager(@Qualifier(value = "biRedisTemplate") RedisTemplate<Object,Object> redisTemplate,
                                            BiLayerCacheProperties biLayerCacheProperties){
        return new BiLayerCacheManager(redisTemplate, biLayerCacheProperties);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        om.activateDefaultTyping(om.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,//类名序列化到json串中
                JsonTypeInfo.As.WRAPPER_ARRAY);

        // 时间序列化
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new JacksonComponent.LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new JacksonComponent.LocalDateTimeDeserializer());

        timeModule.addSerializer(LocalDate.class, new JacksonComponent.LocalDateSerializer());
        timeModule.addDeserializer(LocalDate.class, new JacksonComponent.LocalDateDeserializer());

        timeModule.addSerializer(Date.class, new JacksonComponent.DateSerializer());
        timeModule.addDeserializer(Date.class, new JacksonComponent.DateDeserializer());
        om.registerModule(timeModule);

//        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }
}
