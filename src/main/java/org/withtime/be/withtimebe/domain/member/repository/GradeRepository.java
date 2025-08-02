package org.withtime.be.withtimebe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.member.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
