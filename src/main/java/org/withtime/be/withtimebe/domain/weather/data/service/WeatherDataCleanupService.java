package org.withtime.be.withtimebe.domain.weather.data.service;

import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

public interface WeatherDataCleanupService {

    WeatherSyncResDTO.CleanupResult cleanupOldWeatherData(
            Integer retentionDays, boolean cleanupShortTerm, boolean cleanupMediumTerm, boolean cleanupRecommendations, boolean dryRun);
}
