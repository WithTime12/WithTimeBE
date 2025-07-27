package org.withtime.be.withtimebe.domain.log.dto;

import java.util.List;

import lombok.Builder;

public class PlaceCategoryLogResponseDTO {

	@Builder
	public record WeeklyPlaceCategoryLogList(
		List<WeeklyPlaceCategoryLog> placeCategoryLogList
	) {}

	@Builder
	public record WeeklyPlaceCategoryLog(
		String placeCategoryLabel,
		Integer count
	) {}
}
