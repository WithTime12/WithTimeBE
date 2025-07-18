package org.withtime.be.withtimebe.domain.weather.data.service;

import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeatherRecommendationGenerationService {

    WeatherSyncResDTO.RecommendationGenerationResult generateRecommendations(
            List<Long> regionIds, LocalDate startDate, LocalDate endDate, boolean forceRegenerate, String recommendationType);

    WeatherResDTO.WeeklyRecommendation getWeeklyRecommendation(WeatherReqDTO.GetWeeklyRecommendation request);

    WeatherResDTO.WeeklyPrecipitation getWeeklyPrecipitation(WeatherReqDTO.GetWeeklyPrecipitation request);
}
