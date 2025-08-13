package org.withtime.be.withtimebe.global.config;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.*;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.withtime.be.withtimebe.global.data.RedisConfigData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisConfigData redisConfigData;

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisConfigData.getHost(), redisConfigData.getPort());
    }

    @Bean
    RedisTemplate<String, Object> tokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return  redisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())))
            .entryTtl(Duration.ofDays(1L));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(redisCacheConfiguration).build();
    }

    private ObjectMapper objectMapper() {

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
