package org.withtime.be.withtimebe.domain.weather.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class WeatherSyncReqDTO {

    public record ManualTrigger(
            @NotBlank(message = "작업 타입은 필수입니다.")
            @Pattern(regexp = "^(SHORT_TERM|MEDIUM_TERM|RECOMMENDATION|CLEANUP|ALL)$",
                    message = "올바른 작업 타입을 입력해주세요.")
            String jobType,

            List<Long> targetRegionIds
    ) {
    }
}
