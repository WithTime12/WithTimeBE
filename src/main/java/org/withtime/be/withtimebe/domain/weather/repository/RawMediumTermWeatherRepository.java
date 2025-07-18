package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RawMediumTermWeatherRepository extends JpaRepository<RawMediumTermWeather, Long> {

    Optional<RawMediumTermWeather> findByRegionIdAndBaseDateAndForecastDate(
            Long regionId, LocalDate baseDate, LocalDate forecastDate);

    /**
     * 특정 지역의 특정 날짜 중기 예보 데이터 조회 (분류용)
     */
    @Query("SELECT rmtw FROM RawMediumTermWeather rmtw " +
            "WHERE rmtw.region.id = :regionId " +
            "AND rmtw.forecastDate = :forecastDate " +
            "ORDER BY rmtw.baseDate DESC")
    List<RawMediumTermWeather> findLatestByRegionIdAndForecastDate(
            @Param("regionId") Long regionId,
            @Param("forecastDate") LocalDate forecastDate);
}
