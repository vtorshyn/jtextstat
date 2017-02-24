package com.vtorshyn.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerCommandLineOption {
	String help() default "";

	int defaultValue() default 0;

	boolean mandatory() default false;

	Class type() default Integer.class;
}