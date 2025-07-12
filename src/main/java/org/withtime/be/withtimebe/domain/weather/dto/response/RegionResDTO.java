package org.withtime.be.withtimebe.domain.weather.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Builder
    public record RegionCodeDetail(
            Long regionCodeId,
            String landRegCode,
            String tempRegCode,
            String name,
            String description,
            int regionCount,  // 이 지역코드를 사용하는 지역 수
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }

    @Builder
    public record RegionCodeList(
            List<RegionCodeDetail> regionCodes,
            int totalCount
    ) {
    }
}
