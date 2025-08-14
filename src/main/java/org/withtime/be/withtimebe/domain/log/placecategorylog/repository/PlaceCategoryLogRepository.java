package org.withtime.be.withtimebe.domain.log.placecategorylog.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;

public interface PlaceCategoryLogRepository extends MongoRepository<PlaceCategoryLog, String> {
	List<PlaceCategoryLog> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<PlaceCategoryLog> findByDateAndPlaceCategoryLabelIn(LocalDate date, List<String> placeCategoryLabel);
}
