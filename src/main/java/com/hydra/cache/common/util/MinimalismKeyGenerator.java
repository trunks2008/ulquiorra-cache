package com.hydra.cache.common.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;


/**
 * @author : Hydra
 * @date: 2022/11/8 11:07
 * @version: 1.0
 */
public class MinimalismKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return generateKey(params);
    }

    private Object generateKey(Object[] params) {
        if (params.length == 0) {
            return SimpleKey.EMPTY;
        }

        if (params.length == 1) {
            Object param = params[0];
            Class<?> clazz = param.getClass();
            if (!clazz.isArray()) {
                StringJoiner joiner=new StringJoiner(", ",clazz.getSimpleName()+"(",")");
                Field[] fields = clazz.getDeclaredFields();
                try {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object value = field.get(param);
                        if (value!=null){
                            joiner.add(field.getName()+"="+value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return joiner.toString();
            }
        }

        return new SimpleKey(params);
    }
}
