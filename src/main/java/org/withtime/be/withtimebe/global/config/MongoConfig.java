package org.withtime.be.withtimebe.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.withtime.be.withtimebe.global.converter.MongoConverters;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "org.withtime.be.withtimebe.domain.log")
public class MongoConfig {

	// KST <-> UTC 커스텀 컨버터 빈 등록
	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(List.of(
			new MongoConverters.LocalDateToDateKstConverter(),
			new MongoConverters.LocalTimeToDateKstConverter(),
			new MongoConverters.LocalDateTimeToDateKstConverter(),
			new MongoConverters.DateToLocalDateKstConverter(),
			new MongoConverters.DateToLocalTimeKstConverter(),
			new MongoConverters.DateToLocalDateTimeKstConverter()
		));
	}

	// 커스텀 컨버터 등록 및 _class 필드 제거
	@Bean
	public MappingMongoConverter mappingMongoConverter(
		MongoDatabaseFactory mongoDatabaseFactory,
		MongoMappingContext mongoMappingContext,
		MongoCustomConversions conversions
	) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		converter.setCustomConversions(conversions);
		return converter;
	}
}
