package org.withtime.be.withtimebe.domain.log.placecategorylog.aop;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogPlaceCategoryAspect {

	private final RedisTemplate<String, Object> redisTemplate;

	@Async("logTaskExecutor")
	@AfterReturning("@annotation(org.withtime.be.withtimebe.domain.log.placecategorylog.annotation.LogPlaceCategory)")
	public void logPlaceCategory(JoinPoint joinPoint) {

		Object[] args = joinPoint.getArgs();

		List<String> keywords = Arrays.stream(args)
			.flatMap(arg -> extractKeywordsFromDTO(arg).stream())
			.toList();

		saveKeywords(keywords);
	}

	private List<String> extractKeywordsFromDTO(Object dto) {

		if (dto == null) return Collections.emptyList();
		List<String> keywords = new ArrayList<>();

		for (Field field : dto.getClass().getDeclaredFields()) {
			if (!field.getName().contains("userPreferredKeywords")) continue;

			field.setAccessible(true);
			try {
				Object value = field.get(dto);
				if (value instanceof List<?> list) {
					for (Object keyword : list) {
						if (keyword instanceof String str) {
							keywords.add(str);
						}
					}
				}
			} catch (Exception e) {
				log.warn("[LogPlaceCategoryAspect] DTO에서 userPreferredKeywords 추출 실패", e);
			}
		}
		return keywords;
	}

	private void saveKeywords(List<String> keywords) {
		if (keywords.isEmpty()) return;

		LocalDate now = LocalDate.now();
		String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKey = "log:user-preferred-keywords:" + formattedDate;

		keywords.forEach(keyword ->
			redisTemplate.opsForZSet().incrementScore(redisKey, keyword, 1)
		);

		// TTL 설정
		Long expire = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
		if (expire == null || expire <= 0) {
			redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
		}

		log.info("[LogPlaceCategoryAspect] 키워드 로그 임시 저장 완료");
	}
}
