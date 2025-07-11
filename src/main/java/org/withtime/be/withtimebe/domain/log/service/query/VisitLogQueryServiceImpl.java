package org.withtime.be.withtimebe.domain.log.service.query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.log.entity.VisitLog;
import org.withtime.be.withtimebe.domain.log.repository.VisitLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitLogQueryServiceImpl implements VisitLogQueryService {

	private final VisitLogRepository visitLogRepository;

	@Override
	public List<VisitLog> findDailyVisitLogList() {

		LocalDate dateBefore = LocalDate.now();
		LocalDate dateAfter = dateBefore.minusDays(6);

		return visitLogRepository.findByDateBetween(dateBefore, dateAfter);
	}
}
