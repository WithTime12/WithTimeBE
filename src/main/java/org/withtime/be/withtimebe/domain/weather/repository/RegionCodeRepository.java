package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;

import java.util.List;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Long> {

    boolean existsByLandRegCode(String landRegCode);

    boolean existsByTempRegCode(String tempRegCode);

    @Query("SELECT rc, COUNT(r) FROM RegionCode rc " +
            "LEFT JOIN rc.regions r " +
            "GROUP BY rc " +
            "ORDER BY rc.name ASC")
    List<Object[]> findAllWithRegionCount();

}
