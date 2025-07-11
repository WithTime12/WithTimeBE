package org.withtime.be.withtimebe.domain.log.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

public class VisitLogResponseDTO {

	@Builder
	public record DailyVisitLogList(
		List<DailyVisitLog> dailyVisitLogList
	) {}

	@Builder
	public record DailyVisitLog(
		LocalDate date,
		Long count
	) {}
}
