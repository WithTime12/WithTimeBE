package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * 지역별 날짜 범위 중기예보 배치 조회
     */
    @Query("""
    SELECT rmtw FROM RawMediumTermWeather rmtw 
    WHERE rmtw.region.id = :regionId 
    AND rmtw.forecastDate BETWEEN :startDate AND :endDate
    ORDER BY rmtw.forecastDate ASC, rmtw.baseDate DESC
    """)
    List<RawMediumTermWeather> findByRegionIdAndForecastDateRange(
            @Param("regionId") Long regionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 오래된 중기 예보 데이터 개수 조회 (삭제 대상 확인용)
     * @param cutoffDate 기준 날짜 (이 날짜 이전 예보 대상 데이터가 삭제 대상)
     * @return 삭제 대상 레코드 수
     */
    @Query("SELECT COUNT(rmtw) FROM RawMediumTermWeather rmtw WHERE rmtw.forecastDate < :cutoffDate")
    long countOldData(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * 오래된 중기 예보 데이터 상세 정보 조회 (통계용)
     */
    @Query("SELECT MIN(rmtw.forecastDate), MAX(rmtw.forecastDate), COUNT(rmtw) " +
            "FROM RawMediumTermWeather rmtw WHERE rmtw.forecastDate < :cutoffDate")
    Object[] getOldDataStatistics(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * 오래된 중기 예보 데이터 삭제 (cutoffDate 이전 예보 대상 데이터)
     * @return 삭제된 레코드 수
     */
    @Modifying
    @Query("DELETE FROM RawMediumTermWeather rmtw WHERE rmtw.forecastDate < :cutoffDate")
    int deleteOldData(@Param("cutoffDate") LocalDate cutoffDate);
}
