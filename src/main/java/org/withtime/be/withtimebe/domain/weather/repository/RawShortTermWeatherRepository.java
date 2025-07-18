package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RawShortTermWeatherRepository extends JpaRepository<RawShortTermWeather, Long> {

    Optional<RawShortTermWeather> findByRegionIdAndBaseDateAndBaseTimeAndForecastDateAndForecastTime(
            Long regionId, LocalDate baseDate, String baseTime, LocalDate forecastDate, String forecastTime
    );

    /**
     * 특정 지역의 특정 날짜 예보 데이터 조회 (분류용)
     */
    @Query("SELECT rstw FROM RawShortTermWeather rstw " +
            "WHERE rstw.region.id = :regionId " +
            "AND rstw.forecastDate = :forecastDate " +
            "ORDER BY rstw.baseDate DESC, rstw.baseTime DESC")
    List<RawShortTermWeather> findLatestByRegionIdAndForecastDate(
            @Param("regionId") Long regionId,
            @Param("forecastDate") LocalDate forecastDate);

    /**
     * 지역별 날짜 범위 단기예보 배치 조회
     */
    @Query("""
    SELECT rstw FROM RawShortTermWeather rstw 
    WHERE rstw.region.id = :regionId 
    AND rstw.forecastDate BETWEEN :startDate AND :endDate
    ORDER BY rstw.forecastDate ASC, rstw.baseDate DESC, rstw.baseTime DESC
    """)
    List<RawShortTermWeather> findByRegionIdAndForecastDateRange(
            @Param("regionId") Long regionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
