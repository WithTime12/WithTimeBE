package org.withtime.be.withtimebe.domain.weather.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.weather.config.WeatherClassificationConfig;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherClassificationUtils;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherClassificationServiceImpl implements WeatherClassificationService {

    private final WeatherClassificationConfig config;

    @Override
    public WeatherResDTO.WeatherClassificationResult classifyShortTermWeatherWithCentralTemp(
            List<RawShortTermWeather> shortTermData, Long regionId, LocalDate targetDate) {

        if (shortTermData.isEmpty()) {
            log.warn("단기 예보 데이터가 없습니다: regionId={}, date={}", regionId, targetDate);
            return createDefaultClassification();
        }

        try {
            // 1. 강수확률용 대표 시간대 선택
            RawShortTermWeather representativeData = WeatherClassificationUtils.selectRepresentativeShortTermData(shortTermData, targetDate);

            // 2. 온도 중앙값 계산을 위한 최저/최고 온도 조회
            WeatherClassificationUtils.TemperatureRange tempRange = WeatherClassificationUtils.calculateTemperatureRange(shortTermData, targetDate);

            // 3. 날씨 유형 분류
            WeatherType weatherType = WeatherClassificationUtils.classifyWeatherTypeFromShortTerm(representativeData);

            // 4. 온도 분류 - 중앙값 사용
            TempCategory tempCategory = WeatherClassificationUtils.classifyTempCategoryFromRange(
                    tempRange.minTemp(), tempRange.maxTemp(), config.getTemperature());

            // 5. 강수 분류 - 현재 시간 기준 시간대 사용
            PrecipCategory precipCategory = WeatherClassificationUtils.classifyPrecipCategoryByProbability(
                    representativeData.getPrecipitationProbability(), config.getPrecipitation());

            log.debug("단기예보 중앙값 분류 완료: regionId={}, date={}, weather={}, temp={}, precip={}, 온도범위={}~{}°C, 강수확률={}%",
                    regionId, targetDate, weatherType, tempCategory, precipCategory,
                    tempRange.minTemp(), tempRange.maxTemp(), representativeData.getPrecipitationProbability());

            return new WeatherResDTO.WeatherClassificationResult(weatherType, tempCategory, precipCategory,
                    tempRange.getAvgTemp(), representativeData.getPrecipitationProbability(),
                    representativeData.getPrecipitationAmount(), "단기예보(중앙값)");

        } catch (Exception e) {
            log.error("단기예보 중앙값 분류 중 오류 발생: regionId={}, date={}", regionId, targetDate, e);
            return createDefaultClassification();
        }
    }

    @Override
    public WeatherResDTO.WeatherClassificationResult classifyMediumTermWeather(
            List<RawMediumTermWeather> mediumTermData, Long regionId, LocalDate targetDate) {

        if (mediumTermData.isEmpty()) {
            log.warn("중기 예보 데이터가 없습니다: regionId={}, date={}", regionId, targetDate);
            return createDefaultClassification();
        }

        try {
            RawMediumTermWeather representativeData = WeatherClassificationUtils.selectRepresentativeMediumTermData(mediumTermData, targetDate);

            WeatherType weatherType = WeatherClassificationUtils.classifyWeatherTypeFromMediumTerm(representativeData);
            TempCategory tempCategory = WeatherClassificationUtils.classifyTempCategoryFromRange(
                    representativeData.getMinTemperature(), representativeData.getMaxTemperature(), config.getTemperature());
            PrecipCategory precipCategory = WeatherClassificationUtils.classifyPrecipCategoryByProbability(
                    representativeData.getPrecipitationProbability(), config.getPrecipitation());

            log.debug("중기 예보 분류 완료: regionId={}, date={}, weather={}, temp={}, precip={}, 최저={}°C, 최고={}°C, 강수확률={}%%",
                    regionId, targetDate, weatherType, tempCategory, precipCategory,
                    representativeData.getMinTemperature(), representativeData.getMaxTemperature(), representativeData.getPrecipitationProbability());

            double avgTemp = (representativeData.getMinTemperature() + representativeData.getMaxTemperature()) / 2.0;
            return new WeatherResDTO.WeatherClassificationResult(weatherType, tempCategory, precipCategory,
                    avgTemp, representativeData.getPrecipitationProbability(), 0.0, "중기예보");

        } catch (Exception e) {
            log.error("중기 예보 분류 중 오류 발생: regionId={}, date={}", regionId, targetDate, e);
            return createDefaultClassification();
        }
    }

    private WeatherResDTO.WeatherClassificationResult createDefaultClassification() {
        return new WeatherResDTO.WeatherClassificationResult(
                WeatherType.CLOUDY, TempCategory.MILD, PrecipCategory.NONE,
                20.0, 30.0, 0.0, "기본값");
    }
}

