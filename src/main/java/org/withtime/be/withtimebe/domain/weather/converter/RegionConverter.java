package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionConverter {

    public static RegionCode toRegionCode(RegionReqDTO.CreateRegionCode request) {
        return RegionCode.builder()
                .landRegCode(request.landRegCode())
                .tempRegCode(request.tempRegCode())
                .name(request.name())
                .build();
    }

    public static RegionResDTO.CreateRegionCode toCreateRegionCodeResponse(RegionCode regionCode) {
        return RegionResDTO.CreateRegionCode.builder()
                .regionCodeId(regionCode.getId())
                .landRegCode(regionCode.getLandRegCode())
                .tempRegCode(regionCode.getTempRegCode())
                .name(regionCode.getName())
                .message("지역코드가 성공적으로 등록되었습니다.")
                .build();
    }

    public static Region toRegion(RegionReqDTO.CreateRegion request,
                                  BigDecimal gridX, BigDecimal gridY, RegionCode regionCode) {
        return Region.builder()
                .name(request.name())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .gridX(gridX)
                .gridY(gridY)
                .regionCode(regionCode)
                .build();
    }

    public static RegionResDTO.CreateRegion toCreateRegion(Region region) {
        return RegionResDTO.CreateRegion.builder()
                .regionId(region.getId())
                .name(region.getName())
                .latitude(region.getLatitude())
                .longitude(region.getLongitude())
                .gridX(region.getGridX())
                .gridY(region.getGridY())
                .regionCode(toRegionCodeInfo(region.getRegionCode()))
                .message("지역이 성공적으로 등록되었습니다.")
                .build();
    }

    public static RegionResDTO.RegionCodeInfo toRegionCodeInfo(RegionCode regionCode) {
        return RegionResDTO.RegionCodeInfo.builder()
                .regionCodeId(regionCode.getId())
                .landRegCode(regionCode.getLandRegCode())
                .tempRegCode(regionCode.getTempRegCode())
                .name(regionCode.getName())
                .build();
    }

    public static Region toEntityWithNewCode(RegionReqDTO.CreateRegionWithNewCode request,
                                             BigDecimal gridX, BigDecimal gridY, RegionCode regionCode) {
        return Region.builder()
                .name(request.name())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .gridX(gridX)
                .gridY(gridY)
                .regionCode(regionCode)
                .build();
    }
}
