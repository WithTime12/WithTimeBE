package org.withtime.be.withtimebe.domain.weather.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class WeatherReqDTO {

    public record GetWeeklyRecommendation(
            @NotNull(message = "지역 ID는 필수 입력값입니다.")
            @Positive(message = "지역 ID는 양수여야 합니다.")
            Long regionId,

            @NotNull(message = "시작 날짜는 필수 입력값입니다.")
            LocalDate startDate
    ) {
        /**
         * 시작 날짜 유효성 검증을 포함한 정적 팩토리 메서드
         * 과거 7일 ~ 미래 7일까지만 조회 가능
         */
        public static GetWeeklyRecommendation of(Long regionId, LocalDate startDate) {
            if (startDate != null) {
                LocalDate now = LocalDate.now();
                LocalDate minDate = now.minusDays(7);
                LocalDate maxDate = now.plusDays(7);

                if (startDate.isBefore(minDate) || startDate.isAfter(maxDate)) {
                    throw new IllegalArgumentException(
                            "조회 가능한 시작 날짜 범위를 벗어났습니다. (7일 전 ~ 7일 후)");
                }
            }

            return new GetWeeklyRecommendation(regionId, startDate);
        }

        /**
         * 종료 날짜 계산 (시작일 + 6일)
         */
        public LocalDate getEndDate() {
            return startDate.plusDays(6);
        }
    }

    public record GetWeeklyPrecipitation(
            @NotNull(message = "지역 ID는 필수 입력값입니다.")
            @Positive(message = "지역 ID는 양수여야 합니다.")
            Long regionId,

            @NotNull(message = "시작 날짜는 필수 입력값입니다.")
            LocalDate startDate
    ) {
        public static GetWeeklyPrecipitation of(Long regionId, LocalDate startDate) {
            if (startDate != null) {
                LocalDate now = LocalDate.now();
                LocalDate minDate = now.minusDays(7);
                LocalDate maxDate = now.plusDays(10);

                if (startDate.isBefore(minDate) || startDate.isAfter(maxDate)) {
                    throw new IllegalArgumentException(
                            "조회 가능한 시작 날짜 범위를 벗어났습니다. (7일 전 ~ 10일 후)");
                }
            }

            return new GetWeeklyPrecipitation(regionId, startDate);
        }
        public LocalDate getEndDate() {
            return startDate.plusDays(6);
        }
    }

}
