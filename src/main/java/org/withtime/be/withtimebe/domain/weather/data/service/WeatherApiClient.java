package org.withtime.be.withtimebe.domain.weather.data.service;

import org.withtime.be.withtimebe.domain.weather.entity.Region;

import java.time.LocalDate;

public interface WeatherApiClient {

    String callShortTermWeatherApi(Region region, LocalDate baseDate, String baseTime);

    String callMediumTermLandWeatherApi(Region region, LocalDate tmfc);

    String callMediumTermTempWeatherApi(Region region, LocalDate tmfc);
}
