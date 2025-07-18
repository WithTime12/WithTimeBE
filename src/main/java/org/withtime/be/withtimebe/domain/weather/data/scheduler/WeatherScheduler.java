package org.withtime.be.withtimebe.domain.weather.data.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCleanupService;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCollectionService;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherRecommendationGenerationService;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataHelper;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.weather.enabled", havingValue = "true", matchIfMissing = true)
public class WeatherScheduler {

    private final WeatherDataCollectionService weatherDataCollectionService;
    private final WeatherRecommendationGenerationService weatherRecommendationGenerationService;
    private final WeatherDataCleanupService dataCleanupService;

    // 스케줄러 실행 상태 volatile로 추척
    private volatile boolean shortTermSyncRunning = false;
    private volatile boolean mediumTermSyncRunning = false;
    private volatile boolean shortTermRecommendationRunning = false;
    private volatile boolean mediumTermRecommendationRunning = false;
    private volatile boolean cleanupRunning = false;

    /**
     * 단기 예보 데이터 수집 스케줄러
     * 매 3시간마다 실행 (02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10)
     * 기상청 발표 시각보다 10분 후에 실행하여 데이터 준비 시간 확보
     */
    @Scheduled(cron = "${scheduler.weather.real-short-term-cron}")
    @Async("weatherTaskExecutor")
    public void scheduledShortTermWeatherSync() {
        if (shortTermSyncRunning) {
            log.warn("단기 예보 동기화가 이미 실행 중입니다. 데이터 수집을 스킵합니다.");
            return;
        }

        try {
            shortTermSyncRunning = true;
            log.info("단기 예보 동기화 스케줄러 시작");

            // 올바른 base_date와 base_time 계산
            WeatherDataHelper.BaseDateTime baseDateTime = WeatherDataHelper.calculateBaseDateTime();

            log.debug("계산된 base_date: {}, base_time: {}", baseDateTime.baseDate(), baseDateTime.baseTime());

            // 모든 지역에 대해 동기화 실행
            WeatherSyncResDTO.ShortTermSyncResult result = weatherDataCollectionService.collectShortTermWeatherData(
                    null, baseDateTime.getBaseDateAsLocalDate(), baseDateTime.baseTime(), false);

            log.info("단기 예보 동기화 스케줄러 완료: 성공 {}/{} 지역, 신규 {} 건, 업데이트 {} 건",
                    result.successfulRegions(), result.totalRegions(),
                    result.newDataPoints(), result.updatedDataPoints());

        } catch (Exception e) {
            log.error("단기 예보 동기화 스케줄러 실행 중 오류 발생", e);
        } finally {
            shortTermSyncRunning = false;
        }
    }

    /**
     * 중기 예보 데이터 수집 스케줄러
     * 매 12시간마다 실행 (06:30, 18:30)
     */
    @Scheduled(cron = "${scheduler.weather.real-medium-term-cron}")
    @Async("weatherTaskExecutor")
    public void scheduledMediumTermWeatherSync() {
        if (mediumTermSyncRunning) {
            log.warn("중기 예보 동기화가 이미 실행 중입니다. 데이터 수집을 스킵합니다.");
            return;
        }

        try {
            mediumTermSyncRunning = true;
            log.info("중기 예보 동기화 스케줄러 시작");

            LocalDate tmfc = LocalDate.now();

            // 모든 지역에 대해 동기화 실행
            WeatherSyncResDTO.MediumTermSyncResult result = weatherDataCollectionService.collectMediumTermWeatherData(
                    null, tmfc, false);

            log.info("중기 예보 동기화 스케줄러 완료: 성공 {}/{} 지역, 신규 {} 건, 업데이트 {} 건",
                    result.successfulRegions(), result.totalRegions(),
                    result.newDataPoints(), result.updatedDataPoints());

        } catch (Exception e) {
            log.error("중기 예보 동기화 스케줄러 실행 중 오류 발생", e);
        } finally {
            mediumTermSyncRunning = false;
        }
    }

    /**
     * 단기예보 기반 추천 정보 생성 스케줄러 (0-3일, 실제 단기예보 데이터 범위)
     * 매 시간 5분에 실행 - 단기예보는 1시간마다 업데이트
     */
    @Scheduled(cron = "${scheduler.weather.recommendation.real-short-term-cron}")
    @Async("weatherTaskExecutor")
    public void scheduledShortTermRecommendationGeneration() {
        if (shortTermRecommendationRunning) {
            log.warn("단기예보 추천 생성이 이미 실행 중입니다. 스킵합니다.");
            return;
        }

        try {
            shortTermRecommendationRunning = true;
            log.info("단기예보 추천 생성 스케줄러 시작 (실제 단기예보 데이터 기반)");

            // 오늘부터 4일간만 처리 (실제 단기예보 데이터가 있는 범위)
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(3);

            WeatherSyncResDTO.RecommendationGenerationResult result =
                    weatherRecommendationGenerationService.generateRecommendations(
                            null, startDate, endDate, true, "단기예보");  // 강제 재생성으로 최신 데이터 반영

            log.info("단기예보 추천 생성 스케줄러 완료: 성공 {}/{} 지역, 신규 {} 건, 업데이트 {} 건",
                    result.successfulRegions(), result.totalRegions(),
                    result.newRecommendations(), result.updatedRecommendations());

        } catch (Exception e) {
            log.error("단기예보 추천 생성 스케줄러 실행 중 오류 발생", e);
        } finally {
            shortTermRecommendationRunning = false;
        }
    }

    /**
     * 중기예보 기반 추천 정보 생성 스케줄러 (4-10일, 실제 중기예보 데이터 범위)
     * 매 6시간 30분에 실행 - 중기예보는 12시간마다 업데이트되므로 6시간마다 충분
     */
    @Scheduled(cron = "${scheduler.weather.recommendation.real-medium-term-cron:0 30 0,6,12,18 * * *}")
    @Async("weatherTaskExecutor")
    public void scheduledMediumTermRecommendationGeneration() {
        if (mediumTermRecommendationRunning) {
            log.warn("중기예보 추천 생성이 이미 실행 중입니다. 스킵합니다.");
            return;
        }

        try {
            mediumTermRecommendationRunning = true;
            log.info("중기예보 추천 생성 스케줄러 시작 (실제 중기예보 데이터 기반)");

            // 4일후부터 3일간만 처리 (일반적인 서비스 범위)
            LocalDate startDate = LocalDate.now().plusDays(4);
            LocalDate endDate = LocalDate.now().plusDays(6);

            WeatherSyncResDTO.RecommendationGenerationResult result =
                    weatherRecommendationGenerationService.generateRecommendations(
                            null, startDate, endDate, true, "중기예보");  // 강제 재생성

            log.info("중기예보 추천 생성 스케줄러 완료: 성공 {}/{} 지역, 신규 {} 건, 업데이트 {} 건",
                    result.successfulRegions(), result.totalRegions(),
                    result.newRecommendations(), result.updatedRecommendations());

        } catch (Exception e) {
            log.error("중기예보 추천 생성 스케줄러 실행 중 오류 발생", e);
        } finally {
            mediumTermRecommendationRunning = false;
        }
    }

    /**
     * 데이터 정리 스케줄러
     * 매일 새벽 3시에 실행
     */
    @Scheduled(cron = "${scheduler.weather.real-cleanup-cron}")
    @Async("weatherTaskExecutor")
    public void scheduledDataCleanup() {
        if (cleanupRunning) {
            log.warn("데이터 정리가 이미 실행 중입니다. 스킵합니다.");
            return;
        }

        try {
            cleanupRunning = true;
            log.info("데이터 정리 스케줄러 시작");

            // 7일 이전 데이터 정리
            int retentionDays = 7;
            WeatherSyncResDTO.CleanupResult result = dataCleanupService.cleanupOldWeatherData(
                    retentionDays, true, true, true, false);

            log.info("데이터 정리 스케줄러 완료: 보관기간 {}일, 처리시간 {}ms",
                    retentionDays, result.processingDurationMs());

            if (result.shortTermStats() != null) {
                log.info("단기예보 정리: {} 건 삭제", result.shortTermStats().recordsDeleted());
            }
            if (result.mediumTermStats() != null) {
                log.info("중기예보 정리: {} 건 삭제", result.mediumTermStats().recordsDeleted());
            }
            if (result.recommendationStats() != null) {
                log.info("추천정보 정리: {} 건 삭제", result.recommendationStats().recordsDeleted());
            }

        } catch (Exception e) {
            log.error("데이터 정리 스케줄러 실행 중 오류 발생", e);
        } finally {
            cleanupRunning = false;
        }
    }
}
