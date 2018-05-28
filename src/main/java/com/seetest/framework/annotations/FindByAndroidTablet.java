package com.seetest.framework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.seetest.framework.core.Context;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FindByAndroidTablet {
	String xpath() default "";

}