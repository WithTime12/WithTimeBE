package org.withtime.be.withtimebe.domain.weather.data.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.repository.RawMediumTermWeatherRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RawShortTermWeatherRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherDataHelper {

    // 지역 ID가 없으면 전체, 있으면 ID 기반 조회
    public static List<Region> getTargetRegions(List<Long> regionIds, RegionRepository regionRepository) {
        return (regionIds == null || regionIds.isEmpty())
                ? regionRepository.findAllActiveRegions()
                : regionRepository.findByIdsWithRegionCode(regionIds);
    }

    // 기상청 기준 시각 계산
    public static String calculateNearestBaseTime(int currentHour) {
        int[] baseTimes = {2, 5, 8, 11, 14, 17, 20, 23};

        for (int i = baseTimes.length - 1; i >= 0; i--) {
            if (currentHour >= baseTimes[i]) {
                return String.format("%02d00", baseTimes[i]);
            }
        }
        return "2300";
    }

    public static BaseDateTime calculateBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return calculateBaseDateTime(now);
    }

    public static BaseDateTime calculateBaseDateTime(LocalDateTime targetTime) {
        int currentHour = targetTime.getHour();

        // base_time 계산
        String baseTime = calculateNearestBaseTime(currentHour);

        // base_date 계산
        LocalDate baseDate = targetTime.toLocalDate();

        // 02:00 이전이면 전날 날짜 + 2300 사용
        if (currentHour < 2) {
            baseDate = baseDate.minusDays(1);
            baseTime = "2300";
        }

        String baseDateStr = baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return new BaseDateTime(baseDateStr, baseTime);
    }

    public record BaseDateTime(String baseDate, String baseTime) {
        public LocalDate getBaseDateAsLocalDate() {
            return LocalDate.parse(baseDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public static UpsertResult upsertShortTermWeatherData(
            List<RawShortTermWeather> weatherDataList,
            boolean forceUpdate,
            RawShortTermWeatherRepository repository) {

        int totalProcessed = 0, newRecords = 0, updatedRecords = 0;

        for (RawShortTermWeather data : weatherDataList) {
            Optional<RawShortTermWeather> existingOpt = repository
                    .findByRegionIdAndBaseDateAndBaseTimeAndForecastDateAndForecastTime(
                            data.getRegion().getId(),
                            data.getBaseDate(),
                            data.getBaseTime(),
                            data.getForecastDate(),
                            data.getForecastTime()
                    );

            if (existingOpt.isEmpty()) {
                repository.save(data);
                newRecords++;
            } else if (forceUpdate) {
                existingOpt.get().updateWeatherData(
                        data.getTemperature(), data.getSky(), data.getPrecipitationProbability(),
                        data.getPrecipitationType(), data.getPrecipitationAmount()
                );
                updatedRecords++;
            }
            totalProcessed++;
        }

        return new UpsertResult(totalProcessed, newRecords, updatedRecords);
    }

    public static UpsertResult upsertMediumTermWeatherData(
            List<RawMediumTermWeather> weatherDataList,
            boolean forceUpdate,
            RawMediumTermWeatherRepository repository) {

        int totalProcessed = 0, newRecords = 0, updatedRecords = 0;

        for (RawMediumTermWeather data : weatherDataList) {
            Optional<RawMediumTermWeather> existingOpt = repository
                    .findByRegionIdAndBaseDateAndForecastDate(
                            data.getRegion().getId(),
                            data.getBaseDate(),
                            data.getForecastDate()
                    );

            if (existingOpt.isEmpty()) {
                repository.save(data);
                newRecords++;
            } else if (forceUpdate) {
                existingOpt.get().updateWeatherData(
                        data.getSky(), data.getPrecipitationProbability(),
                        data.getMinTemperature(), data.getMaxTemperature()
                );
                updatedRecords++;
            }
            totalProcessed++;
        }

        return new UpsertResult(totalProcessed, newRecords, updatedRecords);
    }

    public record UpsertResult(int totalProcessed, int newRecords, int updatedRecords) {}
}
