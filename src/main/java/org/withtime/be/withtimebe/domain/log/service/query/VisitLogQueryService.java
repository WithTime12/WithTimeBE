package org.withtime.be.withtimebe.domain.log.service.query;

import java.util.List;

import org.withtime.be.withtimebe.domain.log.entity.VisitLog;

public interface VisitLogQueryService {
	List<VisitLog> findDailyVisitLogList();
}
