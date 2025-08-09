package org.withtime.be.withtimebe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class MapperConfig {

	@Bean
	public ObjectMapper objectMapper() {

		PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
			.allowIfSubType(Object.class)
			.build();

		return new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.registerModule(new JavaTimeModule())   // LocalDateTime 지원 모듈 추가
			.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);  // 클래스 정보를 포함하여 직렬/역직렬화 하도록 설정
	}
}
