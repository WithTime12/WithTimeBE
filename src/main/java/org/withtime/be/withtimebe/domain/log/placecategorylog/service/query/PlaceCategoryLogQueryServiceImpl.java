package org.withtime.be.withtimebe.domain.log.placecategorylog.service.query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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
	public List<PlaceCategoryLog> findWeeklyPlaceCategoryLogList() {

		LocalDate now = LocalDate.now();

		LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		return placeCategoryLogRepository.findByDateBetween(startOfWeek, endOfWeek);
	}
}
