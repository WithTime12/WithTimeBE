package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.weather.entity.Region;

import java.math.BigDecimal;
import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByName(String name);

    @Query("SELECT r FROM Region r WHERE " +
            "ABS(r.latitude - :latitude) < 0.001 AND " +
            "ABS(r.longitude - :longitude) < 0.001")
    List<Region> findByNearCoordinates(@Param("latitude") BigDecimal latitude,
                                       @Param("longitude") BigDecimal longitude);
}
