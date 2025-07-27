package org.withtime.be.withtimebe.domain.log.dateplacelog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.withtime.be.withtimebe.domain.log.dateplacelog.entity.DatePlaceLog;

public interface DatePlaceLogRepository extends MongoRepository<DatePlaceLog, String> {
}
