package org.withtime.be.withtimebe.domain.weather.data.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.withtime.be.withtimebe.domain.weather.config.WeatherClassificationConfig;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherClassificationUtils {

    public static RawShortTermWeather selectRepresentativeShortTermData(List<RawShortTermWeather> dataList, LocalDate targetDate) {
        return dataList.stream()
                .filter(data -> data.getForecastDate().equals(targetDate))
                .sorted((a, b) -> {
                    int baseDateCompare = b.getBaseDate().compareTo(a.getBaseDate());
                    if (baseDateCompare != 0) return baseDateCompare;
                    int baseTimeCompare = b.getBaseTime().compareTo(a.getBaseTime());
                    if (baseTimeCompare != 0) return baseTimeCompare;
                    int aTimeScore = getTimeScore(a.getForecastTime());
                    int bTimeScore = getTimeScore(b.getForecastTime());
                    return Integer.compare(bTimeScore, aTimeScore);
                })
                .findFirst()
                .orElse(dataList.get(0));
    }

    public static RawMediumTermWeather selectRepresentativeMediumTermData(List<RawMediumTermWeather> dataList, LocalDate targetDate) {
        return dataList.stream()
                .filter(data -> data.getForecastDate().equals(targetDate))
                .max(Comparator.comparing(RawMediumTermWeather::getBaseDate))
                .orElse(dataList.get(0));
    }

    private static int getTimeScore(String fcstTime) {
        try {
            LocalTime currentTime = LocalTime.now();

            // 예보 시간 파싱
            int fcstHour = Integer.parseInt(fcstTime.substring(0, 2));
            int fcstMinute = Integer.parseInt(fcstTime.substring(2, 4));
            LocalTime forecastTime = LocalTime.of(fcstHour, fcstMinute);

            // 현재 시간과 예보 시간 차이 계산 (분 단위)
            long timeDifference = Math.abs(ChronoUnit.MINUTES.between(currentTime, forecastTime));

            // 시간 차이가 작을수록 높은 점수 부여
            // 12시간(720분) 이상 차이나면 0점
            if (timeDifference >= 720) {
                return 0;
            }

            // 점수 계산: 최대 1000점에서 시간 차이만큼 감점
            // 720분 차이까지 선형적으로 감소
            int score = Math.max(0, 1000 - (int)(timeDifference * 1000 / 720));

            return score;

        } catch (Exception e) {
            log.warn("시간 점수 계산 중 오류 발생: fcstTime={}", fcstTime, e);
            return 50; // 기본값 반환
        }
    }

    public static WeatherType classifyWeatherTypeFromShortTerm(RawShortTermWeather data) {
        String pty = data.getPrecipitationType();
        String sky = data.getSky();

        return switch (pty) {
            case "눈" -> WeatherType.SNOWY;
            case "비/눈" -> WeatherType.RAIN_SNOW;
            case "비", "빗방울" -> WeatherType.RAINY;
            case "눈날림", "빗방울눈날림" -> WeatherType.SHOWER;
            default -> switch (sky) {
                case "맑음" -> WeatherType.CLEAR;
                case "구름많음", "흐림" -> WeatherType.CLOUDY;
                default -> WeatherType.CLOUDY;
            };
        };
    }

    public static WeatherType classifyWeatherTypeFromMediumTerm(RawMediumTermWeather data) {
        return switch (data.getSky()) {
            case "맑음" -> WeatherType.CLEAR;
            case "구름많음", "흐림" -> WeatherType.CLOUDY;
            case "눈" -> WeatherType.SNOWY;
            case "비" -> WeatherType.RAINY;
            case "소나기" -> WeatherType.SHOWER;
            default -> WeatherType.CLOUDY;
        };
    }

    public static TempCategory classifyTempCategory(Double temperature, WeatherClassificationConfig.TemperatureThresholds thresholds) {
        if (temperature == null) return TempCategory.MILD;
        if (temperature <= thresholds.getChillyCoolBoundary()) return TempCategory.CHILLY;
        else if (temperature <= thresholds.getCoolMildBoundary()) return TempCategory.COOL;
        else if (temperature <= thresholds.getMildHotBoundary()) return TempCategory.MILD;
        else return TempCategory.HOT;
    }

    public static TempCategory classifyTempCategoryFromRange(Double minTemp, Double maxTemp, WeatherClassificationConfig.TemperatureThresholds thresholds) {
        if (minTemp == null || maxTemp == null) return TempCategory.MILD;
        double avgTemp = (minTemp + maxTemp) / 2.0;
        if (maxTemp > thresholds.getMildHotBoundary() + 3) return TempCategory.HOT;
        return classifyTempCategory(avgTemp, thresholds);
    }

    public static PrecipCategory classifyPrecipCategoryByProbability(Double precipProbability, WeatherClassificationConfig.PrecipitationThresholds thresholds) {
        if (precipProbability == null) precipProbability = 0.0;
        if (precipProbability <= thresholds.getNoneVeryLowBoundary()) return PrecipCategory.NONE;
        else if (precipProbability <= thresholds.getVeryLowLowBoundary()) return PrecipCategory.VERY_LOW;
        else if (precipProbability <= thresholds.getLowHighBoundary()) return PrecipCategory.LOW;
        else if (precipProbability <= thresholds.getHighVeryHighBoundary()) return PrecipCategory.HIGH;
        else return PrecipCategory.VERY_HIGH;
    }

    public static TemperatureRange calculateTemperatureRange(List<RawShortTermWeather> dataList, LocalDate targetDate) {
        List<Double> temperatures = dataList.stream()
                .filter(data -> data.getForecastDate().equals(targetDate))
                .map(RawShortTermWeather::getTemperature)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (temperatures.isEmpty()) {
            return new TemperatureRange(20.0, 20.0); // 기본값
        }

        double minTemp = temperatures.stream().mapToDouble(Double::doubleValue).min().orElse(20.0);
        double maxTemp = temperatures.stream().mapToDouble(Double::doubleValue).max().orElse(20.0);

        return new TemperatureRange(minTemp, maxTemp);
    }

    public record TemperatureRange(Double minTemp, Double maxTemp) {
        public Double getAvgTemp() {
            return (minTemp + maxTemp) / 2.0;
        }
    }
}

