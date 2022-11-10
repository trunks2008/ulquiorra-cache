package com.hydra.cache.bilayer.util;

import com.hydra.cache.common.util.SpringUtils;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : Hydra
 * @date: 2022/10/21 14:13
 * @version: 1.0
 */
public class MessageSourceUtil {

    public static String getMsgSource() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        Environment env = SpringUtils.getBean(Environment.class);
        String port = env.getProperty("server.port");
        return host+":"+port;
    }

}
