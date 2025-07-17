package org.withtime.be.withtimebe.domain.weather.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCollectionService;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataHelper;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherSyncReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherTriggerServiceImpl implements WeatherTriggerService{

    private final WeatherDataCollectionService dataCollectionService;

    public WeatherSyncResDTO.ManualTriggerResult triggerAsync(WeatherSyncReqDTO.ManualTrigger request) {
        LocalDateTime triggerTime = LocalDateTime.now();
        String executionId = "manual_" + System.currentTimeMillis();

        CompletableFuture.runAsync(() -> {
            try {
                executeJob(request);
            } catch (Exception e) {
                log.error("비동기 트리거 실패", e);
            }
        });

        return WeatherSyncResDTO.ManualTriggerResult.builder()
                .jobType(request.jobType())
                .triggered(true)
                .executionId(executionId)
                .triggerTime(triggerTime)
                .status("STARTED")
                .message("비동기 작업 시작됨")
                .build();
    }

    private Object executeJob(WeatherSyncReqDTO.ManualTrigger request) {
        log.info("작업 실행 시작: jobType={}", request.jobType());

        return switch (request.jobType()) {
            case "SHORT_TERM" -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDate baseDate = now.toLocalDate();
                String baseTime = WeatherDataHelper.calculateNearestBaseTime(now.getHour());
                yield dataCollectionService.collectShortTermWeatherData(
                        request.targetRegionIds(), baseDate, baseTime, true);  // ← forceExecution = true
            }

            case "MEDIUM_TERM" -> dataCollectionService.collectMediumTermWeatherData(
                    request.targetRegionIds(), LocalDate.now(), true); // ← forceExecution = true

            case "ALL" -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDate baseDate = now.toLocalDate();
                String baseTime = WeatherDataHelper.calculateNearestBaseTime(now.getHour());

                var shortResult = dataCollectionService.collectShortTermWeatherData(
                        request.targetRegionIds(), baseDate, baseTime, true);

                var mediumResult = dataCollectionService.collectMediumTermWeatherData(
                        request.targetRegionIds(), LocalDate.now(), true);

                yield WeatherSyncResDTO.CompleteSyncResult.builder()
                        .shortTermResult(shortResult)
                        .mediumTermResult(mediumResult)
                        .overallStartTime(LocalDateTime.now())
                        .overallEndTime(LocalDateTime.now())
                        .overallDurationMs(0L)
                        .allSuccessful(true)
                        .summaryMessages(List.of("전체 동기화 완료"))
                        .overallStatus("SUCCESS")
                        .build();
            }

            default -> throw new IllegalArgumentException("지원하지 않는 작업 타입: " + request.jobType());
        };
    }
}
