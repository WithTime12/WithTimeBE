package org.withtime.be.withtimebe.domain.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.withtime.be.withtimebe.domain.weather.entity.WeatherTemplate;

import java.util.List;

public interface WeatherTemplateRepository extends JpaRepository<WeatherTemplate, Long> {

    @Query("SELECT wt FROM WeatherTemplate wt " +
            "JOIN FETCH wt.templateKeywords tk " +
            "JOIN FETCH tk.keyword k")
    List<WeatherTemplate> findAllWithKeywords();
}
