package org.withtime.be.withtimebe.domain.log.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.withtime.be.withtimebe.domain.log.model.VisitLog;

public interface VisitLogRepository extends MongoRepository<VisitLog, Long> {
	List<VisitLog> findByDateBetween(LocalDate dateAfter, LocalDate dateBefore);
	List<VisitLog> findByDate(LocalDate date);
}
