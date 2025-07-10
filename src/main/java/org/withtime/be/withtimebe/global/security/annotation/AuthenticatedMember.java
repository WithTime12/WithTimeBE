package org.withtime.be.withtimebe.global.security.annotation;

import java.lang.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@Parameter(hidden = true)
public @interface AuthenticatedMember{
}
