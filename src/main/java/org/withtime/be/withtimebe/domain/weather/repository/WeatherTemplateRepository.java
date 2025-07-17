package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.weather.entity.WeatherTemplate;

public interface WeatherTemplateRepository extends JpaRepository<WeatherTemplate, Long> {
}
