package org.withtime.be.withtimebe.domain.log.datecourselog.dto;

import lombok.Builder;

public class DateCourseLogResponseDTO {

	@Builder
	public record FindAverageDateCourseCount(
		Double averageDateCount,
		Long myDateCount
	) {}

	@Builder
	public record FindSavedDateCourseCount(
		Long count
	) {}
}