package org.withtime.be.withtimebe.domain.weather.data.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataParser {

    private final ObjectMapper objectMapper;

    public List<RawShortTermWeather> parseShortTermWeatherResponse(String jsonResponse, Region region) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            if (!items.isArray()) {
                return Collections.emptyList();
            }

            Map<String, Map<String, String>> groupedData = new HashMap<>();

            for (JsonNode item : items) {
                String baseDate = item.path("baseDate").asText();
                String baseTime = item.path("baseTime").asText();
                String fcstDate = item.path("fcstDate").asText();
                String fcstTime = item.path("fcstTime").asText();
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();

                String key = String.join("_", baseDate, baseTime, fcstDate, fcstTime);
                groupedData.computeIfAbsent(key, k -> new HashMap<>()).put(category, fcstValue);
            }

            List<RawShortTermWeather> results = new ArrayList<>();

            for (Map.Entry<String, Map<String, String>> entry : groupedData.entrySet()) {
                String[] keyParts = entry.getKey().split("_");
                Map<String, String> values = entry.getValue();

                if (hasRequiredCategories(values)) {
                    RawShortTermWeather weather = RawShortTermWeather.builder()
                            .region(region)
                            .baseDate(LocalDate.parse(keyParts[0], DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .baseTime(keyParts[1])
                            .forecastDate(LocalDate.parse(keyParts[2], DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .forecastTime(keyParts[3])
                            .temperature(Double.parseDouble(values.get("TMP")))
                            .sky(convertSkyValue(values.get("SKY")))
                            .precipitationProbability(Double.parseDouble(values.get("POP")))
                            .precipitationType(convertPtyValue(values.get("PTY")))
                            .precipitationAmount(convertPcpValue(values.get("PCP")))
                            .build();

                    results.add(weather);
                }
            }

            log.debug("단기예보 파싱 완료: regionId={}, 파싱된 데이터 수={}", region.getId(), results.size());
            return results;

        } catch (Exception e) {
            log.error("단기 예보 JSON 파싱 실패: regionId={}", region.getId(), e);
            throw new WeatherException(WeatherErrorCode.API_RESPONSE_PARSING_ERROR);
        }
    }

    private boolean hasRequiredCategories(Map<String, String> values) {
        return values.containsKey("TMP") && values.containsKey("SKY") &&
                values.containsKey("POP") && values.containsKey("PTY") && values.containsKey("PCP");
    }

    private String convertSkyValue(String skyCode) {
        return switch (skyCode) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "알수없음";
        };
    }

    private String convertPtyValue(String ptyCode) {
        return switch (ptyCode) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            default -> "알수없음";
        };
    }

    private Double convertPcpValue(String pcpValue) {
        if ("강수없음".equals(pcpValue)) return 0.0;
        try {
            return Double.parseDouble(pcpValue.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
}
