package org.withtime.be.withtimebe.domain.log.placecategorylog.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;
import org.withtime.be.withtimebe.domain.date.repository.PlaceCategoryRepository;
import org.withtime.be.withtimebe.domain.log.placecategorylog.converter.PlaceCategoryLogConverter;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;
import org.withtime.be.withtimebe.domain.log.placecategorylog.repository.PlaceCategoryLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.logs.place-category.enabled", havingValue = "true")
public class PlaceCategoryLogScheduler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final PlaceCategoryLogRepository placeCategoryLogRepository;

	@Scheduled(cron = "${scheduler.logs.place-category.sync-cron}") // 매 5분마다
	@CacheEvict(
		value = "place-category-log",
		key = "'weekly:' + T(java.time.LocalDate).now().getYear() + '-' + T(java.time.temporal.WeekFields).ISO.weekOfYear().getFrom(T(java.time.LocalDate).now())",
		cacheManager = "redisCacheManager",
		beforeInvocation = false
	)
	public void syncUserPreferredKeywordsToDB() {

		log.info("[PlaceCategoryLogScheduler] 동기화 스케쥴러 동작");

		// 현재 날짜 및 레디스 키 생성
		LocalDate now = LocalDate.now();
		String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKey = "log:user-preferred-keywords:" + formattedDate;

		// ZSET 추출
		Set<ZSetOperations.TypedTuple<Object>> zSet =
			redisTemplate.opsForZSet().rangeWithScores(redisKey, 0, -1);

		if (zSet == null || zSet.isEmpty()) {
			log.info("[PlaceCategoryLogScheduler] 저장된 로그가 없어 동기화 작업을 종료합니다.");
			return;
		}

		// 키워드별 점수 추출
		Map<String, Integer> keywordScoreMap = zSet.stream()
			.collect(Collectors.toMap(
				tuple -> String.valueOf(tuple.getValue()),
				tuple -> tuple.getScore().intValue()
			));

		// 키워드를 통해 이미 저장되어 있던 로그 조회
		List<String> keywords = new ArrayList<>(keywordScoreMap.keySet());
		List<PlaceCategoryLog> existingLogs = placeCategoryLogRepository.findByDateAndPlaceCategoryLabelIn(now, keywords);

		// for문에서 빠른 분기 처리를 위한 Map 생성
		Map<String, PlaceCategoryLog> logMap = existingLogs.stream()
			.collect(Collectors.toMap(PlaceCategoryLog::getPlaceCategoryLabel, Function.identity()));

		// saveAll로 저장될 로그 리스트
		List<PlaceCategoryLog> logsToSave = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : keywordScoreMap.entrySet()) {
			String keyword = entry.getKey();
			Integer score = entry.getValue();

			if (logMap.containsKey(keyword)) {
				logMap.get(keyword).incrementCount(score);
			} else {
				PlaceCategoryLog log = PlaceCategoryLogConverter.toPlaceCategoryLog(keyword, score, now);
				logsToSave.add(log);
			}
		}

		// saveAll로 한번에 저장되도록
		if (!logsToSave.isEmpty() || !existingLogs.isEmpty()) {
			placeCategoryLogRepository.saveAll(existingLogs); // 기존 로그 count 증가 반영
			placeCategoryLogRepository.saveAll(logsToSave);   // 신규 로그 저장
			redisTemplate.delete(redisKey);
			log.info("[PlaceCategoryLogScheduler] 키워드 로그 저장 완료, 개수 : {}", logsToSave.size() + existingLogs.size());
		}
	}
}
