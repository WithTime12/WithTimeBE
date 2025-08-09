package org.withtime.be.withtimebe.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.member.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

	@Query("SELECT g FROM Grade g " +
		"WHERE g.requiredPoint <= :point " +
		"ORDER BY g.requiredPoint DESC")
	Optional<Grade> findCurrentGrade(@Param("point") int point);

	@Query("SELECT g FROM Grade g " +
		"WHERE g.requiredPoint > :point " +
		"ORDER BY g.requiredPoint ASC")
	Optional<Grade> findNextGrade(@Param("point") int point);
}
