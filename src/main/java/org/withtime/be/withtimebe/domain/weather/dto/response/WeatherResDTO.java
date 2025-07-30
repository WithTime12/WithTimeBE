package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WeatherResDTO {

    public record WeatherClassificationResult(
            WeatherType weatherType,
            TempCategory tempCategory,
            PrecipCategory precipCategory,
            Double temperature,
            Double precipProbability,
            Double precipAmount,
            String dataSource
    ) {
        public boolean isValid() {
            return weatherType != null && tempCategory != null && precipCategory != null;
        }

        public String getSummary() {
            return String.format("%s, %s (%.1f°C, %.0f%%) [%s]",
                    weatherType, tempCategory, temperature, precipProbability, dataSource);
        }
    }

    /**
     * 날씨 추천 정보 (단일 날짜)
     */
    @Builder
    public record WeatherRecommendation(
            Long recommendationId,
            LocalDate forecastDate,
            RegionInfo region,
            WeatherInfo weather,
            RecommendationInfo recommendation,
            LocalDateTime updatedAt
    ) {
    }


    /**
     * 지역 정보 (날씨 관련 응답용)
     */
    @Builder
    public record RegionInfo(
            Long regionId,
            String regionName,
            String landRegCode,
            String tempRegCode
    ) {
    }

    /**
     * 추천 정보 (메시지, 이모지, 키워드)
     */
    @Builder
    public record RecommendationInfo(
            String message,
            String emoji,
            List<String> keywords
    ) {
    }

    /**
     * 주간 날씨 추천 정보 (7일치)
     */
    @Builder
    public record WeeklyRecommendation(
            RegionInfo region,
            LocalDate startDate,
            LocalDate endDate,
            List<DailyWeatherRecommendation> dailyRecommendations,
            int totalDays,
            String message
    ) {
    }

    /**
     * 날씨 정보 (강수확률 정보 포함)
     */
    @Builder
    public record WeatherInfo(
            WeatherType weatherType,      // CLEAR, CLOUDY, CLOUDY_RAIN 등
            TempCategory tempCategory,    // CHILLY, COOL, MILD, HOT
            PrecipCategory precipCategory, // NONE, VERY_LOW, LOW, HIGH, VERY_HIGH
            String weatherDescription,    // "맑고", "흐리고", "비오고" 등
            String tempDescription,       // "쌀쌀한 날", "선선한 날", "무난한 날", "무더운 날"
            String precipDescription     // "비 없음", "비 거의 없음", "비 약간 가능성" 등
    ) {
    }

    /**
     * 일별 날씨 추천 (강수확률 정보 포함)
     */
    @Builder
    public record DailyWeatherRecommendation(
            LocalDate forecastDate,
            WeatherType weatherType,
            TempCategory tempCategory,
            PrecipCategory precipCategory,
            String message,
            String emoji,
            List<String> keywords
    ) {
    }

    /**
     * 일별 강수확률 정보
     */
    @Builder
    public record DailyPrecipitation(
            LocalDate forecastDate,
            Double precipitationProbability  // 강수확률 (%)
    ) {
    }

    /**
     * 7일간 강수확률 정보
     */
    @Builder
    public record WeeklyPrecipitation(
            RegionInfo region,
            LocalDate startDate,
            LocalDate endDate,
            List<DailyPrecipitation> dailyPrecipitations,
            int totalDays,
            String message
    ) {
    }
}
