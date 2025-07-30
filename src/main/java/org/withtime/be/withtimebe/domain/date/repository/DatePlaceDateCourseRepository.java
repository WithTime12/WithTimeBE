package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.date.entity.DatePlaceDateCourse;

public interface DatePlaceDateCourseRepository extends JpaRepository<DatePlaceDateCourse, Long> {
	@Query("""
		SELECT COUNT(dpc)
		FROM DatePlaceDateCourse dpc
		JOIN dpc.dateCourse dc
		WHERE dc.member.id = :memberId
	""")
	Long countByCreatorMemberId(@Param("memberId") Long memberId);
}
