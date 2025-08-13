package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherSyncConverter {

    public static WeatherSyncResDTO.RegionSyncResult toRegionSyncResult(
            Long regionId, String regionName, boolean success,
            int dataPointsProcessed, int newDataPoints, int updatedDataPoints,
            String errorMessage, long processingTimeMs) {

        return WeatherSyncResDTO.RegionSyncResult.builder()
                .regionId(regionId)
                .regionName(regionName)
                .success(success)
                .dataPointsProcessed(dataPointsProcessed)
                .newDataPoints(newDataPoints)
                .updatedDataPoints(updatedDataPoints)
                .errorMessage(errorMessage)
                .processingTimeMs(processingTimeMs)
                .build();
    }

    public static WeatherSyncResDTO.ShortTermSyncResult toShortTermSyncResult(
            int totalRegions, int successfulRegions, int failedRegions,
            int totalDataPoints, int newDataPoints, int updatedDataPoints,
            LocalDate baseDate, String baseTime,
            LocalDateTime startTime, LocalDateTime endTime,
            List<WeatherSyncResDTO.RegionSyncResult> regionResults,
            List<String> errorMessages) {

        long durationMs = java.time.Duration.between(startTime, endTime).toMillis();
        String message = String.format(
                "단기예보 동기화 완료: 성공 %d/%d 지역, 신규 %d개, 업데이트 %d개 데이터 처리",
                successfulRegions, totalRegions, newDataPoints, updatedDataPoints);

        return WeatherSyncResDTO.ShortTermSyncResult.builder()
                .totalRegions(totalRegions)
                .successfulRegions(successfulRegions)
                .failedRegions(failedRegions)
                .totalDataPoints(totalDataPoints)
                .newDataPoints(newDataPoints)
                .updatedDataPoints(updatedDataPoints)
                .baseDate(baseDate)
                .baseTime(baseTime)
                .processingStartTime(startTime)
                .processingEndTime(endTime)
                .processingDurationMs(durationMs)
                .regionResults(regionResults)
                .errorMessages(errorMessages)
                .message(message)
                .build();
    }

    public static WeatherSyncResDTO.MediumTermSyncResult toMediumTermSyncResult(
            int totalRegions, int successfulRegions, int failedRegions,
            int totalDataPoints, int newDataPoints, int updatedDataPoints,
            LocalDate tmfc, LocalDateTime startTime, LocalDateTime endTime,
            List<WeatherSyncResDTO.RegionSyncResult> regionResults,
            List<String> errorMessages) {

        long durationMs = java.time.Duration.between(startTime, endTime).toMillis();
        String message = String.format(
                "중기예보 동기화 완료: 성공 %d/%d 지역, 신규 %d개, 업데이트 %d개 데이터 처리",
                successfulRegions, totalRegions, newDataPoints, updatedDataPoints);

        return WeatherSyncResDTO.MediumTermSyncResult.builder()
                .totalRegions(totalRegions)
                .successfulRegions(successfulRegions)
                .failedRegions(failedRegions)
                .totalDataPoints(totalDataPoints)
                .newDataPoints(newDataPoints)
                .updatedDataPoints(updatedDataPoints)
                .tmfc(tmfc)
                .processingStartTime(startTime)
                .processingEndTime(endTime)
                .processingDurationMs(durationMs)
                .regionResults(regionResults)
                .errorMessages(errorMessages)
                .message(message)
                .build();
    }

    /**
     * 추천 생성 결과 생성
     */
    public static WeatherSyncResDTO.RecommendationGenerationResult toRecommendationGenerationResult(
            int totalRegions, int successfulRegions, int failedRegions,
            int totalRecommendations, int newRecommendations, int updatedRecommendations,
            LocalDate startDate, LocalDate endDate,
            LocalDateTime startTime, LocalDateTime endTime,
            List<WeatherSyncResDTO.RegionRecommendationResult> regionResults,
            Map<WeatherType, Integer> weatherStats,
            List<String> errorMessages) {

        long durationMs = java.time.Duration.between(startTime, endTime).toMillis();
        String message = String.format(
                "추천 정보 생성 완료: 성공 %d/%d 지역, 신규 %d개, 업데이트 %d개 추천 생성",
                successfulRegions, totalRegions, newRecommendations, updatedRecommendations);

        WeatherSyncResDTO.WeatherTypeStatistics weatherTypeStats = WeatherSyncResDTO.WeatherTypeStatistics.builder()
                .clearWeatherCount(weatherStats.getOrDefault(WeatherType.CLEAR, 0))
                .cloudyWeatherCount(weatherStats.getOrDefault(WeatherType.CLOUDY, 0))
                .cloudyRainCount(weatherStats.getOrDefault(WeatherType.RAINY, 0))
                .cloudySnowCount(weatherStats.getOrDefault(WeatherType.SNOWY, 0))
                .cloudyRainSnowCount(weatherStats.getOrDefault(WeatherType.RAIN_SNOW, 0))
                .cloudyShowerCount(weatherStats.getOrDefault(WeatherType.SHOWER, 0))
                .detailedStats(weatherStats)
                .build();

        return WeatherSyncResDTO.RecommendationGenerationResult.builder()
                .totalRegions(totalRegions)
                .successfulRegions(successfulRegions)
                .failedRegions(failedRegions)
                .totalRecommendations(totalRecommendations)
                .newRecommendations(newRecommendations)
                .updatedRecommendations(updatedRecommendations)
                .startDate(startDate)
                .endDate(endDate)
                .processingStartTime(startTime)
                .processingEndTime(endTime)
                .processingDurationMs(durationMs)
                .regionResults(regionResults)
                .weatherStats(weatherTypeStats)
                .errorMessages(errorMessages)
                .message(message)
                .build();
    }
}
