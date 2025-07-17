package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
