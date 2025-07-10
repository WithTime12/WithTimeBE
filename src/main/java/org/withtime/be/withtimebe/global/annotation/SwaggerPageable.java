package org.withtime.be.withtimebe.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
	@Parameter(
		name = "pageable",
		hidden = true
	),
	@Parameter(
		in = ParameterIn.QUERY,
		name = "page",
		description = "페이지 번호입니다.",
		schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", defaultValue = "0"),
		required = true
	),
	@Parameter(
		in = ParameterIn.QUERY,
		name = "size",
		description = "한 페이지에 표시될 데이터 개수 입니다.",
		schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", defaultValue = "10"),
		required = true
	)
})
public @interface SwaggerPageable {
}