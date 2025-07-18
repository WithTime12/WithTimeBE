package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class WeatherSyncResDTO {

    /**
     * 지역별 동기화 결과
     */
    @Builder
    public record RegionSyncResult(
            Long regionId,
            String regionName,
            boolean success,
            int dataPointsProcessed,
            int newDataPoints,
            int updatedDataPoints,
            String errorMessage,
            long processingTimeMs
    ) {
    }

    /**
     * 단기 예보 동기화 결과 DTO
     */
    @Builder
    public record ShortTermSyncResult(
            int totalRegions,           // 처리된 지역 수
            int successfulRegions,      // 성공한 지역 수
            int failedRegions,          // 실패한 지역 수
            int totalDataPoints,        // 전체 데이터 포인트 수
            int newDataPoints,          // 새로 추가된 데이터 포인트 수
            int updatedDataPoints,      // 업데이트된 데이터 포인트 수
            LocalDate baseDate,         // 기준 날짜
            String baseTime,            // 기준 시간
            LocalDateTime processingStartTime,  // 처리 시작 시간
            LocalDateTime processingEndTime,    // 처리 종료 시간
            long processingDurationMs,          // 처리 소요 시간 (밀리초)
            List<RegionSyncResult> regionResults,  // 지역별 결과
            List<String> errorMessages,         // 오류 메시지들
            String message                      // 전체 결과 메시지
    ) {
    }

    /**
     * 중기 예보 동기화 결과 DTO
     */
    @Builder
    public record MediumTermSyncResult(
            int totalRegions,           // 처리된 지역 수
            int successfulRegions,      // 성공한 지역 수
            int failedRegions,          // 실패한 지역 수
            int totalDataPoints,        // 전체 데이터 포인트 수
            int newDataPoints,          // 새로 추가된 데이터 포인트 수
            int updatedDataPoints,      // 업데이트된 데이터 포인트 수
            LocalDate tmfc,             // 발표 시각
            LocalDateTime processingStartTime,  // 처리 시작 시간
            LocalDateTime processingEndTime,    // 처리 종료 시간
            long processingDurationMs,          // 처리 소요 시간 (밀리초)
            List<RegionSyncResult> regionResults,  // 지역별 결과
            List<String> errorMessages,         // 오류 메시지들
            String message                      // 전체 결과 메시지
    ) {
    }

    /**
     * 수동 트리거 결과 DTO
     */
    @Builder
    public record ManualTriggerResult(
            String jobType,
            boolean triggered,
            String executionId,
            LocalDateTime triggerTime,
            String status,   // STARTED | FAILED
            String message
    ) {
    }

    /**
     * 전체 동기화 결과 DTO (모든 작업 포함)
     */
    @Builder
    public record CompleteSyncResult(
            ShortTermSyncResult shortTermResult,        // 단기 예보 결과
            MediumTermSyncResult mediumTermResult,      // 중기 예보 결과
            RecommendationGenerationResult recommendationResult,  // 추천 생성 결과
            LocalDateTime overallStartTime,             // 전체 시작 시간
            LocalDateTime overallEndTime,               // 전체 종료 시간
            long overallDurationMs,                     // 전체 소요 시간 (밀리초)
            boolean allSuccessful,                      // 모든 작업 성공 여부
            List<String> summaryMessages,               // 요약 메시지들
            String overallStatus                        // 전체 상태
    ) {
    }

    /**
     * 지역별 추천 생성 결과
     */
    @Builder
    public record RegionRecommendationResult(
            Long regionId,
            String regionName,
            boolean success,
            int recommendationsGenerated,
            int newRecommendations,
            int updatedRecommendations,
            List<String> processedDates,  // 처리된 날짜들
            String errorMessage,
            long processingTimeMs
    ) {
    }

    /**
     * 날씨 타입별 통계
     */
    @Builder
    public record WeatherTypeStatistics(
            int clearWeatherCount,
            int cloudyWeatherCount,
            int cloudyRainCount,
            int cloudySnowCount,
            int cloudyRainSnowCount,
            int cloudyShowerCount,
            Map<WeatherType, Integer> detailedStats
    ) {
    }

    /**
     * 추천 정보 생성 결과 DTO
     */
    @Builder
    public record RecommendationGenerationResult(
            int totalRegions,                   // 처리된 지역 수
            int successfulRegions,              // 성공한 지역 수
            int failedRegions,                  // 실패한 지역 수
            int totalRecommendations,           // 전체 생성된 추천 수
            int newRecommendations,             // 새로 생성된 추천 수
            int updatedRecommendations,         // 업데이트된 추천 수
            LocalDate startDate,                // 시작 날짜
            LocalDate endDate,                  // 종료 날짜
            LocalDateTime processingStartTime,  // 처리 시작 시간
            LocalDateTime processingEndTime,    // 처리 종료 시간
            long processingDurationMs,          // 처리 소요 시간 (밀리초)
            List<RegionRecommendationResult> regionResults,  // 지역별 결과
            WeatherTypeStatistics weatherStats, // 날씨별 통계
            List<String> errorMessages,         // 오류 메시지들
            String message                      // 전체 결과 메시지
    ) {
    }

}
