package org.withtime.be.withtimebe.domain.member.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.withtime.be.withtimebe.domain.member.annotation.enums.PointAction;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetPoint {
	PointAction action();
}
