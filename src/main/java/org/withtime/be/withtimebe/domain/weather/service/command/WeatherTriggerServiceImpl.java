package org.withtime.be.withtimebe.domain.weather.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCleanupService;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherDataCollectionService;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherRecommendationGenerationService;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataHelper;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherSyncReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherTriggerServiceImpl implements WeatherTriggerService{

    private final WeatherDataCollectionService dataCollectionService;
    private final WeatherRecommendationGenerationService recommendationGenerationService;
    private final WeatherDataCleanupService dataCleanupService;

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
                // 올바른 base_date와 base_time 계산
                WeatherDataHelper.BaseDateTime baseDateTime = WeatherDataHelper.calculateBaseDateTime();
                log.debug("SHORT_TERM 작업 - 계산된 base_date: {}, base_time: {}",
                        baseDateTime.baseDate(), baseDateTime.baseTime());

                yield dataCollectionService.collectShortTermWeatherData(
                        request.targetRegionIds(), baseDateTime.getBaseDateAsLocalDate(),
                        baseDateTime.baseTime(), true);  // forceExecution = true
            }

            case "MEDIUM_TERM" -> dataCollectionService.collectMediumTermWeatherData(
                    request.targetRegionIds(), LocalDate.now(), true); // forceExecution = true

            case "RECOMMENDATION" -> {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(6);
                yield recommendationGenerationService.generateRecommendations(
                        request.targetRegionIds(), startDate, endDate, true, "일반");
            }

            case "CLEANUP" -> dataCleanupService.cleanupOldWeatherData(
                    7, true, true, true, false);

            case "ALL" -> {
                // 올바른 base_date와 base_time 계산
                WeatherDataHelper.BaseDateTime baseDateTime = WeatherDataHelper.calculateBaseDateTime();
                log.debug("ALL 작업 - 계산된 base_date: {}, base_time: {}",
                        baseDateTime.baseDate(), baseDateTime.baseTime());

                WeatherSyncResDTO.ShortTermSyncResult shortResult = dataCollectionService.collectShortTermWeatherData(
                        request.targetRegionIds(), baseDateTime.getBaseDateAsLocalDate(),
                        baseDateTime.baseTime(), true);

                WeatherSyncResDTO.MediumTermSyncResult mediumResult = dataCollectionService.collectMediumTermWeatherData(
                        request.targetRegionIds(), LocalDate.now(), true);

                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(6);
                WeatherSyncResDTO.RecommendationGenerationResult recommendationResult = recommendationGenerationService.generateRecommendations(
                        request.targetRegionIds(), startDate, endDate, true, "일반");

                WeatherSyncResDTO.CleanupResult cleanupResult = dataCleanupService.cleanupOldWeatherData(
                        7, true, true, true, false);

                yield WeatherSyncResDTO.CompleteSyncResult.builder()
                        .shortTermResult(shortResult)
                        .mediumTermResult(mediumResult)
                        .recommendationResult(recommendationResult)
                        .cleanupResult(cleanupResult)
                        .overallStartTime(LocalDateTime.now())
                        .overallEndTime(LocalDateTime.now())
                        .overallDurationMs(0L)
                        .allSuccessful(true)
                        .summaryMessages(List.of("전체 동기화 완료"))
                        .overallStatus("SUCCESS")
                        .build();
            }

            default -> throw new WeatherException(WeatherErrorCode.UNSUPPORTED_TASK_TYPE);
        };
    }
}
