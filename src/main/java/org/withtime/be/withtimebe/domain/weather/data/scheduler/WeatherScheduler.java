package org.withtime.be.withtimebe.domain.weather.data.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCollectionService;
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

    // 스케줄러 실행 상태 volatile로 추척
    private volatile boolean shortTermSyncRunning = false;
    private volatile boolean mediumTermSyncRunning = false;

    /**
     * 단기 예보 데이터 수집 스케줄러
     * 매 3시간마다 실행 (02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10)
     * 기상청 발표 시각보다 10분 후에 실행하여 데이터 준비 시간 확보
     */
    @Scheduled(cron = "${scheduler.weather.short-term-cron}")
    @Async("weatherTaskExecutor")
    public void scheduledShortTermWeatherSync() {
        if (shortTermSyncRunning) {
            log.warn("단기 예보 동기화가 이미 실행 중입니다. 데이터 수집을 스킵합니다.");
            return;
        }

        try {
            shortTermSyncRunning = true;
            log.info("단기 예보 동기화 스케줄러 시작");

            LocalDateTime now = LocalDateTime.now();
            LocalDate baseDate = now.toLocalDate();
            String baseTime = WeatherDataHelper.calculateNearestBaseTime(now.getHour());

            // 모든 지역에 대해 동기화 실행
            WeatherSyncResDTO.ShortTermSyncResult result = weatherDataCollectionService.collectShortTermWeatherData(
                    null, baseDate, baseTime, false);

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
    @Scheduled(cron = "${scheduler.weather.medium-term-cron}")
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
}
