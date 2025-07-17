package org.withtime.be.withtimebe.domain.log.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.log.model.VisitLog;
import org.withtime.be.withtimebe.domain.log.repository.VisitLogRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.visit-logs.enabled", havingValue = "true")
public class VisitCountScheduler {

	private final RedisTemplate<String, String> redisTemplate;
	private final VisitLogRepository visitLogRepository;

	// 매 정각마다 Redis 방문자 로그를 DB에 저장하는 스케쥴러 (기획 완성되면 변경)
	@Scheduled(cron = "${scheduler.visit-logs.visit-logs-cron}")
	public void saveHourlyVisitCounts() {

		LocalDate today = LocalDate.now();
		Integer hour = LocalTime.now().getHour();

		if (hour == 0) {
			today = today.minusDays(1);
			hour = 23;
		} else {
			hour--;
		}

		// Key
		String redisKey = String.format("visitCount:%s:%02d", today, hour);

		// Set
		Set<String> ipSet = redisTemplate.opsForSet().members(redisKey);

		// Count
		Long count = (ipSet != null) ? ipSet.size() : 0L;

		// Model
		VisitLog visitLog = VisitLog.builder()
			.date(today)
			.hour(LocalTime.of(hour, 0))
			.count(count)
			.build();

		// JPA Save
		visitLogRepository.save(visitLog);

		// Redis Delete
		redisTemplate.delete(redisKey);
	}
}
