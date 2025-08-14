package org.withtime.be.withtimebe.domain.log.placecategorylog.dto;

import java.util.List;

import lombok.Builder;

public class PlaceCategoryLogResponseDTO {

	@Builder
	public record PlaceCategoryLogList(
		List<PlaceCategoryLog> placeCategoryLogList
	) {}

	@Builder
	public record PlaceCategoryLog(
		String placeCategoryLabel,
		Integer count
	) {}
}
