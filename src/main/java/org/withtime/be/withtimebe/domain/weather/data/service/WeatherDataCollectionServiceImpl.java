package org.withtime.be.withtimebe.domain.weather.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.weather.converter.WeatherSyncConverter;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataParser;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.repository.RawShortTermWeatherRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataCollectionServiceImpl implements WeatherDataCollectionService {

    private final WeatherApiClient weatherApiClient;
    private final WeatherDataParser weatherDataParser;
    private final RegionRepository regionRepository;
    private final RawShortTermWeatherRepository shortTermWeatherRepository;

    @Override
    @Transactional
    public WeatherSyncResDTO.ShortTermSyncResult collectShortTermWeatherData(
            List<Long> regionIds, LocalDate baseDate, String baseTime, boolean forceUpdate) {

        LocalDateTime startTime = LocalDateTime.now();
        log.info("단기 예보 수집 시작: regionIds={}, baseDate={}, baseTime={}", regionIds, baseDate, baseTime);

        List<Region> targetRegions = getTargetRegions(regionIds);
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
                UpsertResult upsertResult = upsertShortTermWeatherData(weatherDataList, forceUpdate);

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


    // ==== 내부 유틸리티 메서드들 ====

    private List<Region> getTargetRegions(List<Long> regionIds) {
        return (regionIds == null || regionIds.isEmpty())
                ? regionRepository.findAllActiveRegions()
                : regionRepository.findByIdsWithRegionCode(regionIds);
    }

    private UpsertResult upsertShortTermWeatherData(List<RawShortTermWeather> weatherDataList, boolean forceUpdate) {
        int totalProcessed = 0, newRecords = 0, updatedRecords = 0;

        for (RawShortTermWeather weatherData : weatherDataList) {
            Optional<RawShortTermWeather> existingOpt = shortTermWeatherRepository
                    .findByRegionIdAndBaseDateAndBaseTimeAndFcstDateAndFcstTime(
                            weatherData.getRegion().getId(),
                            weatherData.getBaseDate(),
                            weatherData.getBaseTime(),
                            weatherData.getForecastDate(),
                            weatherData.getForecastTime()
                    );

            if (existingOpt.isEmpty()) {
                shortTermWeatherRepository.save(weatherData);
                newRecords++;
            } else if (forceUpdate) {
                existingOpt.get().updateWeatherData(
                        weatherData.getTemperature(), weatherData.getSky(), weatherData.getPrecipitationProbability(),
                        weatherData.getPrecipitationType(), weatherData.getPrecipitationAmount()
                );
                updatedRecords++;
            }
            totalProcessed++;
        }

        return new UpsertResult(totalProcessed, newRecords, updatedRecords);
    }

    private record UpsertResult(int totalProcessed, int newRecords, int updatedRecords) {}
}

