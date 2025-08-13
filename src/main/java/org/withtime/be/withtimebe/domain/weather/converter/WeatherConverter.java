package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.DailyRecommendation;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.entity.WeatherTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherConverter {

    /**
     * 데이터가 없는 날짜용 빈 DailyWeatherRecommendation 생성
     */
    private static WeatherResDTO.DailyWeatherRecommendation createEmptyDailyRecommendation(LocalDate date) {
        return WeatherResDTO.DailyWeatherRecommendation.builder()
                .forecastDate(date)
                .weatherType(null)
                .tempCategory(null)
                .precipCategory(null)
                .message("해당 날짜의 날씨 추천 정보가 없습니다.")
                .emoji("❓")
                .keywords(List.of("정보없음"))
                .build();
    }

    /**
     * DailyRecommendation을 DailyWeatherRecommendation DTO로 변환
     * 새로운 기획의 구조화된 응답 반영
     */
    public static WeatherResDTO.DailyWeatherRecommendation toDailyWeatherRecommendation(
            DailyRecommendation recommendation, boolean hasRecommendation) {

        if (!hasRecommendation) {
            return createEmptyDailyRecommendation(recommendation.getForecastDate());
        }

        WeatherTemplate template = recommendation.getWeatherTemplate();
        List<String> keywords = template.getTemplateKeywords().stream()
                .map(tk -> tk.getKeyword().getName())
                .distinct()
                .collect(Collectors.toList());

        return WeatherResDTO.DailyWeatherRecommendation.builder()
                .forecastDate(recommendation.getForecastDate())
                .weatherType(template.getWeatherType())
                .tempCategory(template.getTempCategory())
                .precipCategory(template.getPrecipCategory())
                .message(template.getMessage())
                .emoji(template.getEmoji())
                .keywords(keywords)
                .build();
    }

    /**
     * DailyRecommendation 리스트를 WeeklyRecommendation DTO로 변환
     */
    public static WeatherResDTO.WeeklyRecommendation toWeeklyRecommendation(
            List<DailyRecommendation> recommendations, Long regionId, String regionName,
            LocalDate startDate, LocalDate endDate) {

        Map<LocalDate, DailyRecommendation> recommendationMap = recommendations.stream()
                .collect(Collectors.toMap(
                        DailyRecommendation::getForecastDate,
                        rec -> rec
                ));

        List<WeatherResDTO.DailyWeatherRecommendation> dailyRecommendations =
                startDate.datesUntil(endDate.plusDays(1))
                        .map(date -> {
                            DailyRecommendation rec = recommendationMap.get(date);
                            if (rec != null) {
                                return toDailyWeatherRecommendation(rec, true);
                            } else {
                                return createEmptyDailyRecommendation(date);
                            }
                        })
                        .collect(Collectors.toList());

        WeatherResDTO.RegionInfo regionInfo;
        if (!recommendations.isEmpty()) {
            Region region = recommendations.get(0).getRegion();
            regionInfo = toRegionInfo(region);
        } else {
            regionInfo = WeatherResDTO.RegionInfo.builder()
                    .regionId(regionId)
                    .regionName(regionName)
                    .landRegCode(null)
                    .tempRegCode(null)
                    .build();
        }

        return WeatherResDTO.WeeklyRecommendation.builder()
                .region(regionInfo)
                .startDate(startDate)
                .endDate(endDate)
                .dailyRecommendations(dailyRecommendations)
                .totalDays(dailyRecommendations.size())
                .message(String.format("%s 지역의 %s부터 %s까지 주간 날씨 추천입니다.",
                        regionName, startDate, endDate))
                .build();
    }

    /**
     * Region 엔티티를 RegionInfo DTO로 변환
     */
    public static WeatherResDTO.RegionInfo toRegionInfo(Region region) {
        return WeatherResDTO.RegionInfo.builder()
                .regionId(region.getId())
                .regionName(region.getName())
                .landRegCode(region.getRegionCode().getLandRegCode())
                .tempRegCode(region.getRegionCode().getTempRegCode())
                .build();
    }
}
