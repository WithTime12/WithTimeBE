package org.withtime.be.withtimebe.domain.log.placecategorylog.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;

public interface PlaceCategoryLogRepository extends MongoRepository<PlaceCategoryLog, String> {
	@Query("{ 'date': { $gte: ?0, $lte: ?1 } }")
	List<PlaceCategoryLog> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<PlaceCategoryLog> findByDateAndPlaceCategoryLabelIn(LocalDate date, List<String> placeCategoryLabel);
}
