package org.withtime.be.withtimebe.domain.weather.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;

import java.math.BigDecimal;
import java.util.List;

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

    public static RegionResDTO.RegionCodeDetail toRegionCodeDetail(RegionCode regionCode, int regionCount) {
        return RegionResDTO.RegionCodeDetail.builder()
                .regionCodeId(regionCode.getId())
                .landRegCode(regionCode.getLandRegCode())
                .tempRegCode(regionCode.getTempRegCode())
                .name(regionCode.getName())
                .regionCount(regionCount)
                .createdAt(regionCode.getCreatedAt())
                .updatedAt(regionCode.getUpdatedAt())
                .build();
    }

    public static RegionResDTO.RegionCodeList toRegionCodeList(List<Object[]> regionCodesWithCount) {
        List<RegionResDTO.RegionCodeDetail> regionCodeDetails = regionCodesWithCount.stream()
                .map(result -> {
                    RegionCode regionCode = (RegionCode) result[0];
                    Long regionCount = (Long) result[1];
                    return toRegionCodeDetail(regionCode, regionCount.intValue());
                })
                .toList();

        return RegionResDTO.RegionCodeList.builder()
                .regionCodes(regionCodeDetails)
                .totalCount(regionCodeDetails.size())
                .build();
    }

    public static RegionResDTO.RegionInfo toRegionInfo(Region region) {
        return RegionResDTO.RegionInfo.builder()
                .regionId(region.getId())
                .name(region.getName())
                .latitude(region.getLatitude())
                .longitude(region.getLongitude())
                .gridX(region.getGridX())
                .gridY(region.getGridY())
                .regionCode(toRegionCodeInfo(region.getRegionCode()))
                .createdAt(region.getCreatedAt())
                .updatedAt(region.getUpdatedAt())
                .build();
    }

    public static RegionResDTO.RegionList toRegionList(List<Region> regions) {
        List<RegionResDTO.RegionInfo> regionInfos = regions.stream()
                .map(RegionConverter::toRegionInfo)
                .toList();

        return RegionResDTO.RegionList.builder()
                .regions(regionInfos)
                .totalCount(regions.size())
                .build();
    }

    public static RegionResDTO.RegionSearchResult toSearchResult(List<Region> regions, String keyword) {
        List<RegionResDTO.RegionInfo> regionInfos = regions.stream()
                .map(RegionConverter::toRegionInfo)
                .toList();

        return RegionResDTO.RegionSearchResult.builder()
                .regions(regionInfos)
                .keyword(keyword)
                .resultCount(regions.size())
                .build();
    }

    public static RegionResDTO.DeleteRegionCode toDeleteRegionCode(RegionCode regionCode) {
        return RegionResDTO.DeleteRegionCode.builder()
                .regionCodeId(regionCode.getId())
                .name(regionCode.getName())
                .message("지역코드가 성공적으로 삭제되었습니다.")
                .build();
    }

    public static RegionResDTO.DeleteRegion toDeleteRegion(Region region) {
        return RegionResDTO.DeleteRegion.builder()
                .regionId(region.getId())
                .name(region.getName())
                .message("지역이 성공적으로 삭제되었습니다.")
                .build();
    }

    public static RegionResDTO.UserRegion toUserRegion(Region region) {
        return RegionResDTO.UserRegion.builder()
                .regionId(region.getId())
                .name(region.getName())
                .latitude(region.getLatitude())
                .longitude(region.getLongitude())
                .gridX(region.getGridX())
                .gridY(region.getGridY())
                .regionCode(toRegionCodeInfo(region.getRegionCode()))
                .build();
    }

    public static RegionResDTO.UserRegionWithMessage toUserRegionWithMessage(Region region, String message) {
        return RegionResDTO.UserRegionWithMessage.builder()
                .regionId(region.getId())
                .name(region.getName())
                .regionCode(toRegionCodeInfo(region.getRegionCode()))
                .message(message)
                .build();
    }
}
