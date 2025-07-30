package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
}
