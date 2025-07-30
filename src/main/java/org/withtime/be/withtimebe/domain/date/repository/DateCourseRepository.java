package org.withtime.be.withtimebe.domain.date.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {
	Long countByCreatedAtBetween(LocalDate startTime, LocalDate endTime);
	Long countByMemberId(Long memberId);
}
