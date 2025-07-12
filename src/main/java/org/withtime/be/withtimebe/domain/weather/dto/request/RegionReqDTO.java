package org.withtime.be.withtimebe.domain.weather.dto.request;

import jakarta.validation.constraints.NotBlank;

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
}
