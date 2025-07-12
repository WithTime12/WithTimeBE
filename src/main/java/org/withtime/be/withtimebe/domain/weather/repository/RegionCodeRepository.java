package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Long> {

    boolean existsByLandRegCode(String landRegCode);

    boolean existsByTempRegCode(String tempRegCode);
}
