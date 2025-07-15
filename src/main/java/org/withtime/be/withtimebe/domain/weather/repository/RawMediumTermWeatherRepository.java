package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;

import java.time.LocalDate;
import java.util.Optional;

public interface RawMediumTermWeatherRepository extends JpaRepository<RawMediumTermWeather, Long> {

    Optional<RawMediumTermWeather> findByRegionIdAndBaseDateAndForecastDate(
            Long regionId, LocalDate baseDate, LocalDate forecastDate);
}
