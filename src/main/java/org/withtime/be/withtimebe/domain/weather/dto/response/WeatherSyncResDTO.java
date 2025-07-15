package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

}
