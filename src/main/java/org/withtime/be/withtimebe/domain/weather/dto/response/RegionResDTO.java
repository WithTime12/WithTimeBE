package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;

public class RegionResDTO {

    @Builder
    public record CreateRegionCode(
            Long regionCodeId,
            String landRegCode,
            String tempRegCode,
            String name,
            String message
    ) {
    }
}
