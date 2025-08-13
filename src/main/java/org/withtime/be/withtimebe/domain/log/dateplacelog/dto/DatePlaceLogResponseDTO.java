package org.withtime.be.withtimebe.domain.log.dateplacelog.dto;

import java.util.List;

import lombok.Builder;

public class DatePlaceLogResponseDTO {

	@Builder
	public record MonthlyDatePlaceLogList(
		List<MonthlyDatePlaceLog> datePlaceLogList
	) {}

	@Builder
	public record MonthlyDatePlaceLog(
		Long year,
		Long month,
		Long count
	) {}
}
