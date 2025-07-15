package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;

import java.time.LocalDate;
import java.util.Optional;

public interface RawShortTermWeatherRepository extends JpaRepository<RawShortTermWeather, Long> {

    Optional<RawShortTermWeather> findByRegionIdAndBaseDateAndBaseTimeAndForecastDateAndForecastTime(
            Long regionId, LocalDate baseDate, String baseTime, LocalDate forecastDate, String forecastTime
    );
}
