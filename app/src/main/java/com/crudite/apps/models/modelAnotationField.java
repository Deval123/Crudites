package com.crudite.apps.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface modelAnotationField {
	String columnName() default "";
	String tableName() default "";
	String sqlQuery() default "";
	String columnType() default "";
	boolean primaryKey() default false;
	boolean downloadRemoteFile() default false;
	String remoteFileUrl() default "";
}
