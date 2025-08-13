package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;

import java.time.LocalDateTime;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long>, DateCourseRepositoryCustom {
	Long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
	Long countByMemberId(Long memberId);
}
