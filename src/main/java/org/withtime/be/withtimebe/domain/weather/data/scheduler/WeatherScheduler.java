package org.withtime.be.withtimebe.domain.weather.data.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCollectionService;
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
            String baseTime = calculateNearestBaseTime(now.getHour());

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

    // === 내부 유틸리티 메서드 ===
    private String calculateNearestBaseTime(int currentHour) {
        int[] baseTimes = {2, 5, 8, 11, 14, 17, 20, 23};

        // 현재 시각보다 이전 또는 같은 가장 가까운 기준시각 찾기
        for (int i = baseTimes.length - 1; i >= 0; i--) {
            if (currentHour >= baseTimes[i]) {
                return String.format("%02d00", baseTimes[i]);
            }
        }
        // 현재 시각이 새벽 2시 이전이면 전날의 23시
        return "2300";
    }
}
