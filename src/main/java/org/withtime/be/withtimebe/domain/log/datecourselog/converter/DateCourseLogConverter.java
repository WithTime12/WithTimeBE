package org.withtime.be.withtimebe.domain.log.datecourselog.converter;

import org.withtime.be.withtimebe.domain.log.datecourselog.dto.DateCourseLogResponseDTO;

public class DateCourseLogConverter {

	public static DateCourseLogResponseDTO.FindAverageDateCourseCount toFindAverageDateCourseCount(Double averageDateCount, Long myDateCount) {
		return DateCourseLogResponseDTO.FindAverageDateCourseCount.builder()
			.averageDateCount(averageDateCount)
			.myDateCount(myDateCount)
			.build();
	}
}
