package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.GradeResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Grade;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public class GradeConverter {

	public static GradeResponseDTO.FindMyGrade toFindMyGrade(Member member, Grade current, Grade next) {

		int nextRequiredPoint = (next == null) ? 0 : next.getRequiredPoint() - member.getPoint();

		return GradeResponseDTO.FindMyGrade.builder()
			.username(member.getUsername())
			.grade(current.getGradeType().name())
			.level(current.getLevel())
			.description(current.getDescription())
			.nextRequiredPoint(nextRequiredPoint)
			.build();
	}
}
