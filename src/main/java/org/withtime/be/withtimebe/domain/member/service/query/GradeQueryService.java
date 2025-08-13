package org.withtime.be.withtimebe.domain.member.service.query;

import org.withtime.be.withtimebe.domain.member.dto.GradeResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface GradeQueryService {
	GradeResponseDTO.FindMyGrade findMyGrade(Member member);
}
