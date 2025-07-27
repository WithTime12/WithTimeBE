package org.withtime.be.withtimebe.domain.log.dateplacelog.service.query;

import java.util.List;

import org.withtime.be.withtimebe.domain.log.dateplacelog.entity.DatePlaceLog;

public interface DatePlaceLogQueryService {
	List<DatePlaceLog> findMonthlyDatePlaceLogList();
}
