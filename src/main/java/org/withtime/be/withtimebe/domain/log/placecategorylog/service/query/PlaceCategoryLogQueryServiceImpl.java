package org.withtime.be.withtimebe.domain.log.placecategorylog.service.query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;
import org.withtime.be.withtimebe.domain.log.placecategorylog.repository.PlaceCategoryLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceCategoryLogQueryServiceImpl implements PlaceCategoryLogQueryService {

	private final PlaceCategoryLogRepository placeCategoryLogRepository;

	@Override
	@Cacheable(
		value = "place-category-log",
		key = "'weekly:' + T(java.time.LocalDate).now().getYear() + '-' + T(java.time.temporal.WeekFields).ISO.weekOfYear().getFrom(T(java.time.LocalDate).now())",
		cacheManager = "redisCacheManager"
	)
	public List<PlaceCategoryLog> findWeeklyPlaceCategoryLogList() {

		LocalDate now = LocalDate.now();

		LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
		LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);

		return placeCategoryLogRepository.findByDateBetween(startOfWeek, endOfWeek);
	}
}
