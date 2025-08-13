package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * 오래된 단기 예보 데이터 개수 조회 (삭제 대상 확인용)
     * @param cutoffDate 기준 날짜 (이 날짜 이전 예보 대상 데이터가 삭제 대상)
     * @return 삭제 대상 레코드 수
     */
    @Query("SELECT COUNT(rstw) FROM RawShortTermWeather rstw WHERE rstw.forecastDate < :cutoffDate")
    long countOldData(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * 오래된 단기 예보 데이터 상세 정보 조회 (통계용)
     * @param cutoffDate 기준 날짜
     * @return [최오래된예보날짜, 최신예보날짜, 레코드수]
     */
    @Query("SELECT MIN(rstw.forecastDate), MAX(rstw.forecastDate), COUNT(rstw) " +
            "FROM RawShortTermWeather rstw WHERE rstw.forecastDate < :cutoffDate")
    Object[] getOldDataStatistics(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * 오래된 단기 예보 데이터 삭제 (cutoffDate 이전 예보 대상 데이터)
     * @return 삭제된 레코드 수
     */
    @Modifying
    @Query("DELETE FROM RawShortTermWeather rstw WHERE rstw.forecastDate < :cutoffDate")
    int deleteOldData(@Param("cutoffDate") LocalDate cutoffDate);
}
