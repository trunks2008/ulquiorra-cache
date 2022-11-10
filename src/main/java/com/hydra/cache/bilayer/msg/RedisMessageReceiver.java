package com.hydra.cache.bilayer.msg;

import com.hydra.cache.bilayer.core.BiLayerCache;
import com.hydra.cache.bilayer.core.BiLayerCacheManager;
import com.hydra.cache.bilayer.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
@Slf4j
@AllArgsConstructor
public class RedisMessageReceiver {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final BiLayerCacheManager manager;

    //接收通知，进行处理
    public void receive(String message) throws UnknownHostException {
        CacheMassage msg = (CacheMassage) redisTemplate
                .getValueSerializer().deserialize(message.getBytes());
        log.debug(msg.toString());

        //如果是本机发出的消息，那么不进行处理
        if (msg.getMsgSource().equals(MessageSourceUtil.getMsgSource())){
            log.info("Receive message sent by local machine, do nothing.");
            return;
        }

        BiLayerCache cache = (BiLayerCache) manager.getCache(msg.getCacheName());
        if (msg.getType()== CacheMsgType.UPDATE) {
            cache.updateL1Cache(msg.getKey(),msg.getValue());
            log.info("update local cache");
        }

        if (msg.getType()== CacheMsgType.DELETE) {
            cache.evictL1Cache(msg.getKey());
            log.info("delete local cache");
        }
    }
}
