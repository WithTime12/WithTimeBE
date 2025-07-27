package org.withtime.be.withtimebe.domain.log.dateplacelog.scheduler;

import java.time.LocalDate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.date.repository.DatePlaceRepository;
import org.withtime.be.withtimebe.domain.log.dateplacelog.converter.DatePlaceLogConverter;
import org.withtime.be.withtimebe.domain.log.dateplacelog.entity.DatePlaceLog;
import org.withtime.be.withtimebe.domain.log.dateplacelog.repository.DatePlaceLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.logs.date-place.enabled", havingValue = "true")
public class DatePlaceLogScheduler {

	private final DatePlaceRepository datePlaceRepository;
	private final DatePlaceLogRepository datePlaceLogRepository;

	@Scheduled(cron = "${scheduler.logs.date-place.sync-cron}")
	@Transactional(readOnly = true)
	public void syncPlaceCategoryLogsToDB() {

		LocalDate now = LocalDate.now();
		Long count = datePlaceRepository.count();

		DatePlaceLog datePlaceLog = DatePlaceLogConverter.toDatePlaceLog(now, count);
		datePlaceLogRepository.save(datePlaceLog);

		log.info("[DatePlaceLogScheduler] {} - 누적 데이트 장소 {}건 저장 완료", now, count);
	}
}
