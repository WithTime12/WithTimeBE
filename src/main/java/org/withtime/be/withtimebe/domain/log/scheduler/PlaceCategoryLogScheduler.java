package org.withtime.be.withtimebe.domain.log.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;
import org.withtime.be.withtimebe.domain.date.repository.PlaceCategoryRepository;
import org.withtime.be.withtimebe.domain.log.model.PlaceCategoryLog;
import org.withtime.be.withtimebe.domain.log.repository.PlaceCategoryLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.logs.place-category.enabled", havingValue = "true")
public class PlaceCategoryLogScheduler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final PlaceCategoryLogRepository placeCategoryLogRepository;
	private final PlaceCategoryRepository placeCategoryRepository;

	@Scheduled(cron = "${scheduler.logs.place-category.sync-cron}") // 매 5분마다
	public void syncPlaceCategoryLogsToDB() {

		// 현재 날짜 및 레디스 키 생성
		LocalDate now = LocalDate.now();
		String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKey = "log:place-category:" + formattedDate;

		// ZSET 추출
		Set<ZSetOperations.TypedTuple<Object>> zSet =
			redisTemplate.opsForZSet().rangeWithScores(redisKey, 0, -1);

		// 없는 경우 Skip
		if (zSet == null || zSet.isEmpty())
			return;

		// ZSET 데이터 가공
		Map<Long, Integer> placeCategoryScoreMap = zSet.stream()
			.collect(Collectors.toMap(
				tuple -> Long.valueOf(String.valueOf(tuple.getValue())),
				tuple -> tuple.getScore().intValue()
			));

		// IN 쿼리로 이미 존재하는 PlaceCategoryLog 조회
		List<Long> placeCategoryIds = new ArrayList<>(placeCategoryScoreMap.keySet());
		List<PlaceCategoryLog> placeCategoryLogList = placeCategoryLogRepository.findByPlaceCategoryIdInAndDate(placeCategoryIds, now);
		
		// placeCategoryId - placeCategoryLog 매핑
		Map<Long, PlaceCategoryLog> placeCategoryLogMap = placeCategoryLogList.stream()
			.collect(Collectors.toMap(PlaceCategoryLog::getPlaceCategoryId, Function.identity()));

		// 레디스 데이터를 다큐먼트에 반영
		for (Map.Entry<Long, Integer> entry : placeCategoryScoreMap.entrySet()) {
			Long placeCategoryId = entry.getKey();
			Integer score = entry.getValue();

			// 기존 로그가 존재하는 경우 count만 증가
			if (placeCategoryLogMap.containsKey(placeCategoryId)) {
				placeCategoryLogMap.get(placeCategoryId).incrementCount(score);
			}
			// 그렇지 않으면 새로 로그 생성
			else {
				Optional<PlaceCategory> placeCategoryOptional = placeCategoryRepository.findById(placeCategoryId);
				if(placeCategoryOptional.isPresent()) {
					PlaceCategory placeCategory = placeCategoryOptional.get();
					PlaceCategoryLog newPlaceCategoryLog = PlaceCategoryLog.builder()
						.placeCategoryId(placeCategory.getId())
						.placeCategoryLabel(placeCategory.getLabel())
						.count(score)
						.date(now)
						.build();
					placeCategoryLogList.add(newPlaceCategoryLog);
				}
			}
		}
		
		// 다큐먼트를 DB에 반영
		if (!placeCategoryLogList.isEmpty()) {
			placeCategoryLogRepository.saveAll(placeCategoryLogList);
			redisTemplate.delete(redisKey);
			log.info("[PlaceCategoryLogScheduler] 카테고리 로그 저장 완료, 개수 : {}", placeCategoryLogList.size());
		}
	}
}
