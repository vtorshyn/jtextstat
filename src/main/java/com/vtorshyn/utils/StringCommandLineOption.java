package com.vtorshyn.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface StringCommandLineOption {
	String help() default "";

	String defaultValue() default "";

	boolean mandatory() default false;

	Class<?> type() default String.class;
}