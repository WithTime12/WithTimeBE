package org.withtime.be.withtimebe.domain.log.datecourselog.service.query;

import org.withtime.be.withtimebe.domain.log.datecourselog.dto.DateCourseLogResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DateCourseLogQueryService {
	DateCourseLogResponseDTO.FindAverageDateCourseCount findAverageDateCourseCount(Member member);
	DateCourseLogResponseDTO.FindSavedDateCourseCount findSavedDateCourseCount(Member member);
}
