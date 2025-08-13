package org.withtime.be.withtimebe.domain.log.dateplacelog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.withtime.be.withtimebe.domain.log.dateplacelog.model.DatePlaceLog;

public interface DatePlaceLogRepository extends MongoRepository<DatePlaceLog, String> {
}
