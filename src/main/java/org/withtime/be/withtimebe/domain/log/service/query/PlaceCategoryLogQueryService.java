package org.withtime.be.withtimebe.domain.log.service.query;

import java.util.List;

import org.withtime.be.withtimebe.domain.log.model.PlaceCategoryLog;

public interface PlaceCategoryLogQueryService {
	List<PlaceCategoryLog> findWeeklyPlaceCategoryLogList();
}
