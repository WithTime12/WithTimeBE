package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

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

    @Builder
    public record RegionCodeInfo(
            Long regionCodeId,
            String landRegCode,
            String tempRegCode,
            String name
    ) {
    }

    @Builder
    public record CreateRegion(
            Long regionId,
            String name,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal gridX,
            BigDecimal gridY,
            RegionCodeInfo regionCode,
            String message
    ) {
    }
}
