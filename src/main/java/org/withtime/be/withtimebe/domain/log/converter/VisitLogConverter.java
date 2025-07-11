package org.withtime.be.withtimebe.domain.log.converter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.withtime.be.withtimebe.domain.log.dto.response.VisitLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.entity.VisitLog;

public class VisitLogConverter {

	public static VisitLogResponseDTO.DailyVisitLogList toDailyVisitLogList(List<VisitLog> visitLogList) {

		Map<LocalDate, Long> result = visitLogList.stream()
			.collect(Collectors.groupingBy(VisitLog::getDate, Collectors.summingLong(VisitLog::getCount)));

		List<VisitLogResponseDTO.DailyVisitLog> dailyVisitLogList = result.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(entry -> toDailyVisitLog(entry.getKey(), entry.getValue()))
			.toList();

		return VisitLogResponseDTO.DailyVisitLogList.builder()
			.dailyVisitLogList(dailyVisitLogList)
			.build();
	}

	public static VisitLogResponseDTO.DailyVisitLog toDailyVisitLog(LocalDate localDate, Long totalCount) {

		return VisitLogResponseDTO.DailyVisitLog.builder()
			.date(localDate)
			.count(totalCount)
			.build();
	}
}
