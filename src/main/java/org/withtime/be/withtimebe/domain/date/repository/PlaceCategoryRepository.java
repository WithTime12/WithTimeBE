package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;

import java.util.Collection;
import java.util.List;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
    List<PlaceCategory> findAllByLabelIn(List<String> labels);
}
