package org.withtime.be.withtimebe.domain.weather.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClientImpl implements WeatherApiClient {

    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.short-term-forecast-url}")
    private String shortTermForecastUrl;

    @Override
    public String callShortTermWeatherApi(Region region, LocalDate baseDate, String baseTime) {
        try {
            int gridX = region.getGridX().intValue();
            int gridY = region.getGridY().intValue();

            log.debug("단기예보 API 호출: regionId={}, gridX={}, gridY={}, baseDate={}, baseTime={}",
                    region.getId(), gridX, gridY, baseDate, baseTime);

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(shortTermForecastUrl)
                            .queryParam("authKey", apiKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 1052)
                            .queryParam("dataType", "JSON")
                            .queryParam("base_date", baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .queryParam("base_time", baseTime)
                            .queryParam("nx", gridX)
                            .queryParam("ny", gridY)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null || response.trim().isEmpty()) {
                throw new WeatherException(WeatherErrorCode.SHORT_TERM_FORECAST_ERROR);
            }

            log.debug("단기예보 API 응답 수신 완료: regionId={}, 응답길이={}", region.getId(), response.length());
            return response;

        } catch (Exception e) {
            log.error("단기예보 API 호출 실패: regionId={}, gridX={}, gridY={}, baseDate={}, baseTime={}",
                    region.getId(), region.getGridX(), region.getGridY(), baseDate, baseTime, e);
            throw new WeatherException(WeatherErrorCode.SHORT_TERM_FORECAST_ERROR);
        }
    }
}
