package org.withtime.be.withtimebe.domain.weather.data.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.weather.entity.RawMediumTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.RawShortTermWeather;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataParser {

    private final ObjectMapper objectMapper;

    /**
     * 단기 예보 JSON 응답 파싱
     */
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

    /**
     * 중기 예보 텍스트 응답 파싱
     */
    public List<RawMediumTermWeather> parseMediumTermWeatherResponse(
            String landResponse, String tempResponse, Region region) {
        try {
            Map<String, MediumTermLandData> landDataMap = parseMediumTermLandData(landResponse);
            Map<String, MediumTermTempData> tempDataMap = parseMediumTermTempData(tempResponse);

            List<RawMediumTermWeather> results = new ArrayList<>();

            for (String key : landDataMap.keySet()) {
                MediumTermLandData landData = landDataMap.get(key);
                MediumTermTempData tempData = tempDataMap.get(key);

                if (landData != null && tempData != null) {
                    try {
                        Double pop = parseDoubleValue(landData.rnSt(), "강수확률");
                        Double minTmp = parseDoubleValue(tempData.min(), "최저기온");
                        Double maxTmp = parseDoubleValue(tempData.max(), "최고기온");

                        if (pop != null && minTmp != null && maxTmp != null) {
                            RawMediumTermWeather weather = RawMediumTermWeather.builder()
                                    .region(region)
                                    .baseDate(LocalDate.parse(landData.tmfc().substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                    .forecastDate(LocalDate.parse(landData.tmef().substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                    .sky(convertMediumTermSkyValue(landData.sky()))
                                    .precipitationProbability(pop)
                                    .minTemperature(minTmp)
                                    .maxTemperature(maxTmp)
                                    .build();

                            results.add(weather);
                            log.debug("중기예보 파싱 성공: key={}, pop={}, minTmp={}, maxTmp={}",
                                    key, pop, minTmp, maxTmp);
                        } else {
                            log.warn("중기예보 데이터 불완전하여 스킵: key={}, pop={}, minTmp={}, maxTmp={}",
                                    key, landData.rnSt(), tempData.min(), tempData.max());
                        }
                    } catch (Exception e) {
                        log.warn("중기예보 개별 데이터 파싱 실패 (스킵): key={}, landData={}, tempData={}, error={}",
                                key, landData, tempData, e.getMessage());
                    }
                }
            }

            log.info("중기예보 파싱 완료: regionId={}, 성공 {}/{} 건",
                    region.getId(), results.size(), landDataMap.size());
            return results;

        } catch (Exception e) {
            log.error("중기 예보 텍스트 파싱 실패: regionId={}", region.getId(), e);
            throw new WeatherException(WeatherErrorCode.API_RESPONSE_PARSING_ERROR);
        }
    }

    // ===== 단기 예보 관련 메서드들 =====
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

    // ===== 중기 예보 관련 메서드들 =====
    private Map<String, MediumTermLandData> parseMediumTermLandData(String response) {
        Map<String, MediumTermLandData> result = new HashMap<>();

        try {
            Pattern pattern = Pattern.compile("#START7777(.*?)#7777END", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                String data = matcher.group(1);
                String[] lines = data.split("\n");

                for (String line : lines) {
                    if (line.trim().isEmpty() || line.startsWith("#")) continue;

                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 11) {
                        MediumTermLandData landData = new MediumTermLandData(
                                parts[1],  // TM_FC
                                parts[2],  // TM_EF
                                parts[6],  // SKY
                                parts[10]  // RN_ST
                        );

                        String key = parts[1] + "_" + parts[2];
                        result.put(key, landData);
                    }
                }
            }
        } catch (Exception e) {
            log.error("중기 육상예보 파싱 실패", e);
        }

        log.debug("중기 육상예보 파싱 완료: {} 건", result.size());
        return result;
    }

    private Map<String, MediumTermTempData> parseMediumTermTempData(String response) {
        Map<String, MediumTermTempData> result = new HashMap<>();

        try {
            Pattern pattern = Pattern.compile("#START7777(.*?)#7777END", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                String data = matcher.group(1);
                String[] lines = data.split("\n");

                for (String line : lines) {
                    if (line.trim().isEmpty() || line.startsWith("#")) continue;

                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 8) {
                        try {
                            MediumTermTempData tempData = new MediumTermTempData(
                                    parts[1],  // TM_FC
                                    parts[2],  // TM_EF
                                    parts[6],  // MIN
                                    parts[7]   // MAX
                            );

                            String key = parts[1] + "_" + parts[2];
                            result.put(key, tempData);

                            log.trace("중기 기온예보 라인 파싱: key={}, min={}, max={}",
                                    key, parts[6], parts[7]);
                        } catch (Exception e) {
                            log.warn("중기 기온예보 라인 파싱 실패 (스킵): line='{}', error={}",
                                    line.trim(), e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("중기 기온예보 전체 파싱 실패", e);
        }

        log.debug("중기 기온예보 파싱 완료: {} 건", result.size());
        return result;
    }

    private String convertMediumTermSkyValue(String skyCode) {
        return switch (skyCode) {
            case "WB01" -> "맑음";
            case "WB03" -> "구름많음";
            case "WB04" -> "흐림";
            case "WB13", "WB12" -> "눈";
            default -> "알수없음";
        };
    }

    private Double parseDoubleValue(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            log.debug("{} 값이 비어있음", fieldName);
            return null;
        }

        String trimmedValue = value.trim();

        if (trimmedValue.matches("^[A-Z]\\d+$")) {
            log.debug("{} 코드값 감지: {} -> 기본값 사용", fieldName, trimmedValue);
            return getDefaultValueForCode(trimmedValue, fieldName);
        }

        if (!trimmedValue.matches("^-?\\d*\\.?\\d+$")) {
            log.warn("{} 파싱 불가능한 값: {} -> null 반환", fieldName, trimmedValue);
            return null;
        }

        try {
            double parsedValue = Double.parseDouble(trimmedValue);

            if (!isValidValue(parsedValue, fieldName)) {
                log.warn("{} 값이 유효 범위를 벗어남: {} -> null 반환", fieldName, parsedValue);
                return null;
            }

            return parsedValue;
        } catch (NumberFormatException e) {
            log.warn("{} 숫자 파싱 실패: {} -> null 반환", fieldName, trimmedValue);
            return null;
        }
    }

    private Double getDefaultValueForCode(String code, String fieldName) {
        return switch (fieldName) {
            case "강수확률" -> 30.0;
            case "최저기온" -> 15.0;
            case "최고기온" -> 25.0;
            default -> {
                log.debug("알 수 없는 필드명: {}, 코드: {} -> 0.0 반환", fieldName, code);
                yield 0.0;
            }
        };
    }

    private boolean isValidValue(double value, String fieldName) {
        return switch (fieldName) {
            case "강수확률" -> value >= 0.0 && value <= 100.0;
            case "최저기온" -> value >= -50.0 && value <= 50.0;
            case "최고기온" -> value >= -50.0 && value <= 50.0;
            default -> true;
        };
    }

    // ===== 내부 데이터 클래스들 =====
    private record MediumTermLandData(String tmfc, String tmef, String sky, String rnSt) {}
    private record MediumTermTempData(String tmfc, String tmef, String min, String max) {}
}
