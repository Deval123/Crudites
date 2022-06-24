package com.crudite.apps.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface modelAnotationType {
	int synchOrder() default 0;
	boolean uploadData() default false;
	boolean downloadData() default true;
	String remoteSqlQuery() default "";
	String tableName() default "";
	String childTable() default "";
}
