package org.withtime.be.withtimebe.domain.log.dateplacelog.service.query;

import java.util.List;

import org.withtime.be.withtimebe.domain.log.dateplacelog.model.DatePlaceLog;

public interface DatePlaceLogQueryService {
	List<DatePlaceLog> findMonthlyDatePlaceLogList();
}
