package org.withtime.be.withtimebe.domain.dateplace.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;

public interface DatePlaceQueryService {
	Page<DatePlace> findDatePlaces(Pageable pageable);
}
