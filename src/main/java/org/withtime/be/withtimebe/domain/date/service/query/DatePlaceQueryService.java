package org.withtime.be.withtimebe.domain.date.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;

public interface DatePlaceQueryService {
	Page<DatePlace> findDatePlaces(Pageable pageable);
}
