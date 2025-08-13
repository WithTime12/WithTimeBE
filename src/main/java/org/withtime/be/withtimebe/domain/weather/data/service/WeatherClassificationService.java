package org.withtime.be.withtimebe.domain.weather.data.service;

import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;

import java.time.LocalDate;
import java.util.List;

public interface WeatherClassificationService {

    WeatherResDTO.WeatherClassificationResult classifyShortTermWeatherWithCentralTemp(
            List<RawShortTermWeather> shortTermData, Long regionId, LocalDate targetDate);

    WeatherResDTO.WeatherClassificationResult classifyMediumTermWeather(
            List<RawMediumTermWeather> mediumTermData, Long regionId, LocalDate targetDate);
}
