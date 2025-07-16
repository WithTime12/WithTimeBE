package org.withtime.be.withtimebe.domain.dateplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;

public interface DatePlaceRepository extends JpaRepository<DatePlace, Long> {
}
