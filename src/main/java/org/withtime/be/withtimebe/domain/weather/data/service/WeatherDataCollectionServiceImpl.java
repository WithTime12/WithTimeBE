package org.withtime.be.withtimebe.domain.weather.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.weather.converter.WeatherSyncConverter;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataHelper;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataParser;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.repository.RawMediumTermWeatherRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RawShortTermWeatherRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataCollectionServiceImpl implements WeatherDataCollectionService {

    private final WeatherApiClient weatherApiClient;
    private final WeatherDataParser weatherDataParser;
    private final RegionRepository regionRepository;
    private final RawShortTermWeatherRepository rawShortTermWeatherRepository;
    private final RawMediumTermWeatherRepository rawMediumTermWeatherRepository;

    /**
     * 단기 예보 데이터 수집 및 저장
     */
    @Override
    @Transactional
    public WeatherSyncResDTO.ShortTermSyncResult collectShortTermWeatherData(
            List<Long> regionIds, LocalDate baseDate, String baseTime, boolean forceUpdate) {

        LocalDateTime startTime = LocalDateTime.now();
        log.info("단기 예보 수집 시작: regionIds={}, baseDate={}, baseTime={}", regionIds, baseDate, baseTime);

        List<Region> targetRegions = WeatherDataHelper.getTargetRegions(regionIds, regionRepository);
        List<WeatherSyncResDTO.RegionSyncResult> regionResults = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        int totalDataPoints = 0, newRecords = 0, updatedDataPoints = 0;
        int successfulRegions = 0, failedRegions = 0;

        for (Region region : targetRegions) {
            long regionStartTime = System.currentTimeMillis();

            try {
                log.debug("지역 {} 단기 예보 수집 시작", region.getName());

                String response = weatherApiClient.callShortTermWeatherApi(region, baseDate, baseTime);
                List<RawShortTermWeather> weatherDataList = weatherDataParser.parseShortTermWeatherResponse(response, region);
                WeatherDataHelper.UpsertResult upsertResult = WeatherDataHelper.upsertShortTermWeatherData(
                        weatherDataList, forceUpdate, rawShortTermWeatherRepository);

                totalDataPoints += upsertResult.totalProcessed();
                newRecords += upsertResult.newRecords();
                updatedDataPoints += upsertResult.updatedRecords();
                successfulRegions++;

                regionResults.add(WeatherSyncConverter.toRegionSyncResult(
                        region.getId(), region.getName(), true,
                        upsertResult.totalProcessed(), upsertResult.newRecords(), upsertResult.updatedRecords(),
                        null, System.currentTimeMillis() - regionStartTime));

                log.debug("지역 {} 단기 예보 수집 완료: 신규 {}, 업데이트 {}",
                        region.getName(), upsertResult.newRecords(), upsertResult.updatedRecords());

            } catch (Exception e) {
                failedRegions++;
                String errorMessage = String.format("지역 %s 처리 실패: %s", region.getName(), e.getMessage());
                errorMessages.add(errorMessage);

                regionResults.add(WeatherSyncConverter.toRegionSyncResult(
                        region.getId(), region.getName(), false, 0, 0, 0,
                        errorMessage, System.currentTimeMillis() - regionStartTime));

                log.error("지역 {} 단기 예보 수집 실패", region.getName(), e);
            }
        }

        LocalDateTime endTime = LocalDateTime.now();
        log.info("단기 예보 수집 완료: 성공 {}/{} 지역, 신규 {}, 업데이트 {} 데이터",
                successfulRegions, targetRegions.size(), newRecords, updatedDataPoints);

        return WeatherSyncConverter.toShortTermSyncResult(
                targetRegions.size(), successfulRegions, failedRegions,
                totalDataPoints, newRecords, updatedDataPoints,
                baseDate, baseTime, startTime, endTime, regionResults, errorMessages);
    }

    /**
     * 중기 예보 데이터 수집 및 저장
     */
    @Override
    @Transactional
    public WeatherSyncResDTO.MediumTermSyncResult collectMediumTermWeatherData(
            List<Long> regionIds, LocalDate tmfc, boolean forceUpdate) {

        LocalDateTime startTime = LocalDateTime.now();
        log.info("중기 예보 수집 시작: regionIds={}, tmfc={}", regionIds, tmfc);

        List<Region> targetRegions = WeatherDataHelper.getTargetRegions(regionIds, regionRepository);
        List<WeatherSyncResDTO.RegionSyncResult> regionResults = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        int totalDataPoints = 0, newDataPoints = 0, updatedDataPoints = 0;
        int successfulRegions = 0, failedRegions = 0;

        for (Region region : targetRegions) {
            long regionStartTime = System.currentTimeMillis();

            try {
                log.debug("지역 {} 중기 예보 수집 시작", region.getName());

                String landResponse = CompletableFuture.supplyAsync(() ->
                        weatherApiClient.callMediumTermLandWeatherApi(region, tmfc)).get();
                String tempResponse = CompletableFuture.supplyAsync(() ->
                        weatherApiClient.callMediumTermTempWeatherApi(region, tmfc)).get();

                List<RawMediumTermWeather> weatherDataList = weatherDataParser.parseMediumTermWeatherResponse(
                        landResponse, tempResponse, region);
                WeatherDataHelper.UpsertResult upsertResult = WeatherDataHelper.upsertMediumTermWeatherData(
                        weatherDataList, forceUpdate, rawMediumTermWeatherRepository);

                totalDataPoints += upsertResult.totalProcessed();
                newDataPoints += upsertResult.newRecords();
                updatedDataPoints += upsertResult.updatedRecords();
                successfulRegions++;

                regionResults.add(WeatherSyncConverter.toRegionSyncResult(
                        region.getId(), region.getName(), true,
                        upsertResult.totalProcessed(), upsertResult.newRecords(), upsertResult.updatedRecords(),
                        null, System.currentTimeMillis() - regionStartTime));

                log.debug("지역 {} 중기 예보 수집 완료: 신규 {}, 업데이트 {}",
                        region.getName(), upsertResult.newRecords(), upsertResult.updatedRecords());

            } catch (Exception e) {
                failedRegions++;
                String errorMessage = String.format("지역 %s 처리 실패: %s", region.getName(), e.getMessage());
                errorMessages.add(errorMessage);

                regionResults.add(WeatherSyncConverter.toRegionSyncResult(
                        region.getId(), region.getName(), false, 0, 0, 0,
                        errorMessage, System.currentTimeMillis() - regionStartTime));

                log.error("지역 {} 중기 예보 수집 실패", region.getName(), e);
            }
        }

        LocalDateTime endTime = LocalDateTime.now();
        log.info("중기 예보 수집 완료: 성공 {}/{} 지역, 신규 {}, 업데이트 {} 데이터",
                successfulRegions, targetRegions.size(), newDataPoints, updatedDataPoints);

        return WeatherSyncConverter.toMediumTermSyncResult(
                targetRegions.size(), successfulRegions, failedRegions,
                totalDataPoints, newDataPoints, updatedDataPoints,
                tmfc, startTime, endTime, regionResults, errorMessages);
    }
}

