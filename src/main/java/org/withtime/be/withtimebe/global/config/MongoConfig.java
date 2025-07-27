package org.withtime.be.withtimebe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "org.withtime.be.withtimebe.domain.log")
public class MongoConfig {
}
