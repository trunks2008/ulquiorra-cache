package com.hydra.cache.common.annotation;

import com.hydra.cache.common.switcher.BiSwitch;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BiSwitch.class)
public @interface EnableBiLayerCache {
}
