package org.withtime.be.withtimebe.domain.log.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.withtime.be.withtimebe.domain.log.model.PlaceCategoryLog;

public interface PlaceCategoryLogRepository extends MongoRepository<PlaceCategoryLog, String> {
	List<PlaceCategoryLog> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<PlaceCategoryLog> findByPlaceCategoryIdInAndDate(List<Long> placeCategoryIds, LocalDate date);
}
