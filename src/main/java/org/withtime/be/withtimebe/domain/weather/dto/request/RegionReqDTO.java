package org.withtime.be.withtimebe.domain.weather.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class RegionReqDTO {

    public record CreateRegionCode(
            @NotBlank(message = "중기 육상 예보 지역코드는 필수 입력값입니다.")
            String landRegCode,

            @NotBlank(message = "중기 기온 예보 지역코드는 필수 입력값입니다.")
            String tempRegCode,

            @NotBlank(message = "지역코드명은 필수 입력값입니다.")
            String name
    ) {
    }

    public record CreateRegion(
            @NotBlank(message = "지역명은 필수 입력값입니다.")
            String name,

            @NotNull(message = "위도는 필수 입력값입니다.")
            BigDecimal latitude,

            @NotNull(message = "경도는 필수 입력값입니다.")
            BigDecimal longitude,

            @NotNull(message = "지역코드 ID는 필수 입력값입니다.")
            @Positive(message = "지역코드 ID는 양수여야 합니다.")
            Long regionCodeId
    ) {
    }

    public record CreateRegionWithNewCode(
            @NotBlank(message = "지역명은 필수 입력값입니다.")
            String name,

            @NotNull(message = "위도는 필수 입력값입니다.")
            BigDecimal latitude,

            @NotNull(message = "경도는 필수 입력값입니다.")
            BigDecimal longitude,

            @NotBlank(message = "중기 육상 예보 지역코드는 필수 입력값입니다.")
            String landRegCode,

            @NotBlank(message = "중기 기온 예보 지역코드는 필수 입력값입니다.")
            String tempRegCode,

            @NotBlank(message = "지역코드명은 필수 입력값입니다.")
            String regionCodeName
    ) {
    }

    public record UpdateUserRegion(
            @NotNull(message = "지역 ID는 필수 입력값입니다.")
            Long regionId
    ) {
    }
}
