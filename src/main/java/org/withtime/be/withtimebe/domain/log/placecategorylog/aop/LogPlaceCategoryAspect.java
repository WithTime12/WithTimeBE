package org.withtime.be.withtimebe.domain.log.placecategorylog.aop;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogPlaceCategoryAspect {

	private final RedisTemplate<String, Object> redisTemplate;

	@Before("@annotation(org.withtime.be.withtimebe.domain.log.placecategorylog.annotation.LogPlaceCategory)")
	public void logPlaceCategory(JoinPoint joinPoint) {
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = signature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		List<Long> placeCategoryIds = IntStream.range(0, args.length)
			.mapToObj(i -> extractIdsFromParam(paramNames[i], args[i]))
			.flatMap(Collection::stream)
			.toList();

		savePlaceCategoryIds(placeCategoryIds);
	}

	// 1. 파라미터로부터 placeCategoryId 추출
	private List<Long> extractIdsFromParam(String paramName, Object arg) {

		if (paramName.contains("placeCategoryId") && arg instanceof Long id) {
			return List.of(id);
		}

		if (paramName.contains("placeCategoryId") && arg instanceof List<?> list) {
			return list.stream()
				.filter(Long.class::isInstance)
				.map(Long.class::cast)
				.toList();
		}

		// DTO로 간주하고 추출 시도
		return extractIdsFromDTO(arg);
	}

	// 2. DTO로부터 placeCategoryId 추출
	private List<Long> extractIdsFromDTO(Object dto) {

		if (dto == null) return Collections.emptyList();
		List<Long> ids = new ArrayList<>();

		// DTO에 정의된 필드에 접근
		for (Field field : dto.getClass().getDeclaredFields()) {
			if (!field.getName().contains("placeCategoryId")) continue;

			field.setAccessible(true);	// private 필드 접근 설정
			try {
				Object value = field.get(dto);	// 값 추출
				if (value instanceof Long placeCategoryId) {
					ids.add(placeCategoryId);
				}
				else if (value instanceof List<?> list) {
					for (Object id : list) {
						if (id instanceof Long placeCategoryId) {
							ids.add(placeCategoryId);
						}
					}
				}
			} catch (Exception e) {
				log.warn("DTO에서 placeCategoryId 추출 실패");
			}
		}
		return ids;
	}

	private void savePlaceCategoryIds(List<Long> placeCategoryIds) {
		if (placeCategoryIds.isEmpty()) return;

		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKey = "log:place-category:" + today;

		// ZSET - 카테고리 별 검색 횟수 기록
		placeCategoryIds
			.forEach(id -> redisTemplate.opsForZSet().incrementScore(redisKey, id, 1));

		// TTL - 이번 주까지로 설정
		Long expire = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
		if (expire == null || expire <= 0) {
			LocalDateTime endOfWeek = now.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
			Duration duration = Duration.between(now, endOfWeek);
			redisTemplate.expire(redisKey, duration.getSeconds(), TimeUnit.SECONDS);
		}
	}
}
