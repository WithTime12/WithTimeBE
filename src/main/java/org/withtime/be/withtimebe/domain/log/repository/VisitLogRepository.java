package org.withtime.be.withtimebe.domain.log.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.log.entity.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
	List<VisitLog> findByDateBetween(LocalDate dateAfter, LocalDate dateBefore);
	List<VisitLog> findByDate(LocalDate date);
}
