package org.withtime.be.withtimebe.domain.log.placecategorylog.service.query;

import java.util.List;

import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;

public interface PlaceCategoryLogQueryService {
	List<PlaceCategoryLog> findWeeklyPlaceCategoryLogList();
}
