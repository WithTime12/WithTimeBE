package org.withtime.be.withtimebe.domain.member.dto;

import lombok.Builder;

public record GradeResponseDTO() {

	@Builder
	public record FindMyGrade(
		String username,
		String grade,
		String level,
		String description,
		Integer nextRequiredPoint
	) {}
}
