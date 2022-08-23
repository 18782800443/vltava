package com.dmall.vltava.domain.annotation;

import com.dmall.vltava.domain.base.CommonResult;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rob
 * @date Create in 10:14 AM 2019/11/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleException {
    Class resultType() default CommonResult.class;
}
