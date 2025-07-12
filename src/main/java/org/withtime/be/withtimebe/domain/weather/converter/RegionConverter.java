package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;

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
}
