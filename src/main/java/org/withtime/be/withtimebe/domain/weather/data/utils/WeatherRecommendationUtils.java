package org.withtime.be.withtimebe.domain.weather.data.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.WeatherTemplate;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherRecommendationUtils {

    public static String createTemplateKey(WeatherType weatherType, TempCategory tempCategory, PrecipCategory precipCategory) {
        return String.format("%s_%s_%s", weatherType, tempCategory, precipCategory);
    }

    public static Map<String, WeatherTemplate> createTemplateMap(List<WeatherTemplate> templates) {
        return templates.stream().collect(Collectors.toMap(
                t -> createTemplateKey(t.getWeatherType(), t.getTempCategory(), t.getPrecipCategory()),
                t -> t,
                (existing, duplicate) -> {
                    log.debug("중복 템플릿 키 유지: {}", existing.getId());
                    return existing;
                }
        ));
    }

    public static void mergeWeatherStats(Map<WeatherType, Integer> globalStats,
                                         Map<WeatherType, Integer> regionStats) {
        for (Map.Entry<WeatherType, Integer> entry : regionStats.entrySet()) {
            globalStats.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public static WeatherTemplate findMatchingTemplate(
            WeatherResDTO.WeatherClassificationResult classification,
            Map<String, WeatherTemplate> templateMap) {

        String key = createTemplateKey(classification.weatherType(), classification.tempCategory(), classification.precipCategory());
        WeatherTemplate template = templateMap.get(key);

        if (template != null) {
            log.trace("정확 템플릿 매칭 성공: {} -> ID={}", key, template.getId());
            return template;
        }

        log.debug("정확 매칭 실패: {}, 대체 템플릿 탐색 시작", key);
        return findFallbackTemplate(classification, templateMap);
    }

    private static WeatherTemplate findFallbackTemplate(
            WeatherResDTO.WeatherClassificationResult classification,
            Map<String, WeatherTemplate> templateMap) {

        for (PrecipCategory fallback : generatePrecipFallbackOrder(classification.precipCategory())) {
            String altKey = createTemplateKey(classification.weatherType(), classification.tempCategory(), fallback);
            WeatherTemplate altTemplate = templateMap.get(altKey);
            if (altTemplate != null) {
                log.debug("대체 템플릿 매칭: {} -> ID={} (from {})", altKey, altTemplate.getId(), classification.precipCategory());
                return altTemplate;
            }
        }

        return findByWeatherAndTempOnly(classification.weatherType(), classification.tempCategory(), templateMap);
    }

    private static WeatherTemplate findByWeatherAndTempOnly(WeatherType weatherType, TempCategory tempCategory,
                                                            Map<String, WeatherTemplate> templateMap) {
        for (PrecipCategory category : PrecipCategory.values()) {
            String key = createTemplateKey(weatherType, tempCategory, category);
            WeatherTemplate template = templateMap.get(key);
            if (template != null) return template;
        }
        return null;
    }

    private static List<PrecipCategory> generatePrecipFallbackOrder(PrecipCategory current) {
        return switch (current) {
            case VERY_HIGH -> List.of(PrecipCategory.VERY_HIGH, PrecipCategory.HIGH, PrecipCategory.LOW, PrecipCategory.VERY_LOW, PrecipCategory.NONE);
            case HIGH -> List.of(PrecipCategory.HIGH, PrecipCategory.VERY_HIGH, PrecipCategory.LOW, PrecipCategory.VERY_LOW, PrecipCategory.NONE);
            case LOW -> List.of(PrecipCategory.LOW, PrecipCategory.HIGH, PrecipCategory.VERY_LOW, PrecipCategory.VERY_HIGH, PrecipCategory.NONE);
            case VERY_LOW -> List.of(PrecipCategory.VERY_LOW, PrecipCategory.LOW, PrecipCategory.NONE, PrecipCategory.HIGH, PrecipCategory.VERY_HIGH);
            case NONE -> List.of(PrecipCategory.NONE, PrecipCategory.VERY_LOW, PrecipCategory.LOW, PrecipCategory.HIGH, PrecipCategory.VERY_HIGH);
        };
    }
}
