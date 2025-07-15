package org.withtime.be.withtimebe.domain.weather.data.service;

import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeatherDataCollectionService {

    WeatherSyncResDTO.ShortTermSyncResult collectShortTermWeatherData(
            List<Long> regionIds, LocalDate baseDate, String baseTime, boolean forceUpdate);

    WeatherSyncResDTO.MediumTermSyncResult collectMediumTermWeatherData(
            List<Long> regionIds, LocalDate tmfc, boolean forceUpdate);
}
