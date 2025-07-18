package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.weather.entity.DailyRecommendation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRecommendationRepository extends JpaRepository<DailyRecommendation, Long> {

    /**
     * 특정 지역, 특정 날짜의 추천 정보 조회
     * WeatherTemplate, Keyword 정보까지 함께 fetch join
     */
    @Query("SELECT dr FROM DailyRecommendation dr " +
            "JOIN FETCH dr.weatherTemplate wt " +
            "JOIN FETCH wt.templateKeywords tk " +
            "JOIN FETCH tk.keyword k " +
            "WHERE dr.region.id = :regionId " +
            "AND dr.forecastDate = :date")
    Optional<DailyRecommendation> findByRegionIdAndDateWithTemplate(
            @Param("regionId") Long regionId,
            @Param("date") LocalDate date);

    /**
     * 특정 지역의 주간 추천 정보 조회 (7일치)
     * 시작 날짜부터 7일간의 데이터 조회
     */
    @Query("SELECT dr FROM DailyRecommendation dr " +
            "JOIN FETCH dr.weatherTemplate wt " +
            "JOIN FETCH wt.templateKeywords tk " +
            "JOIN FETCH tk.keyword k " +
            "WHERE dr.region.id = :regionId " +
            "AND dr.forecastDate >= :startDate " +
            "AND dr.forecastDate < :endDate " +
            "ORDER BY dr.forecastDate ASC")
    List<DailyRecommendation> findWeeklyRecommendations(
            @Param("regionId") Long regionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
