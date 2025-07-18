package org.withtime.be.withtimebe.domain.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class WeatherClassificationConfig {

    private TemperatureThresholds temperature = new TemperatureThresholds();
    private PrecipitationThresholds precipitation = new PrecipitationThresholds();

    /**
     * 기온 분류 임계값
     * 새로운 기획: 중앙값 기준
     */
    @Getter
    @Setter
    public static class TemperatureThresholds {
        // 쌀쌀한 날씨 ≤ 10℃
        private double chillyCoolBoundary = 10.0;

        // 선선한 날씨 11~20℃
        private double coolMildBoundary = 20.0;

        // 무난한 날씨 21~25℃
        private double mildHotBoundary = 25.0;

        // 무더운 날씨 ≥ 26℃
    }

    /**
     * 강수 분류 임계값
     * 새로운 기획: 강수확률 기반
     */
    @Getter
    @Setter
    public static class PrecipitationThresholds {
        // 비 없음: 0%
        private double noneVeryLowBoundary = 0.0;

        // 비 거의 없음: 1~30%
        private double veryLowLowBoundary = 30.0;

        // 비 약간 가능성: 31~60%
        private double lowHighBoundary = 60.0;

        // 비 올 가능성 높음: 61~90%
        private double highVeryHighBoundary = 90.0;

        // 비 확실: 91~100%

        // 기존 강수량 임계값도 유지 (단기예보에서 사용할 수 있음)
        private double lightAmountThreshold = 1.0;   // 1mm 이상 가벼운 비
        private double heavyAmountThreshold = 10.0;  // 10mm 이상 많은 비
    }
}
