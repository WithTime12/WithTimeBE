package org.withtime.be.withtimebe.domain.log.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
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

	@Builder
	public record HourlyVisitLogList(
		LocalDate date,
		List<HourlyVisitLog> hourlyVisitLogList
	) {}

	@Builder
	public record HourlyVisitLog(
		LocalTime hour,
		Long count
	) {}
}
