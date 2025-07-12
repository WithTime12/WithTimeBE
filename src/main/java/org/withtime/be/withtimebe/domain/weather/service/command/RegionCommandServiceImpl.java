package org.withtime.be.withtimebe.domain.weather.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.weather.converter.RegionConverter;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;
import org.withtime.be.withtimebe.domain.weather.repository.RegionCodeRepository;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionCommandServiceImpl implements RegionCommandService {

    private final RegionCodeRepository regionCodeRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.grid-conversion-url}")
    private String gridConversionUrl;

    @Override
    public RegionResDTO.CreateRegionCode createRegionCode(RegionReqDTO.CreateRegionCode request) {
        log.info("지역코드 등록 요청: {}", request.name());

        validateDuplicateRegionCode(request.landRegCode(), request.tempRegCode());

        RegionCode regionCode = RegionConverter.toRegionCode(request);
        RegionCode savedRegionCode = regionCodeRepository.save(regionCode);

        log.info("지역코드 등록 완료: {} (ID: {})", savedRegionCode.getName(), savedRegionCode.getId());
        return RegionConverter.toCreateRegionCodeResponse(savedRegionCode);
    }

    private void validateDuplicateRegionCode(String landRegCode, String tempRegCode) {
        if (regionCodeRepository.existsByLandRegCode(landRegCode)) {
            throw new WeatherException(WeatherErrorCode.REGION_ALREADY_EXISTS);
        }
        if (regionCodeRepository.existsByTempRegCode(tempRegCode)) {
            throw new WeatherException(WeatherErrorCode.REGION_ALREADY_EXISTS);
        }
    }
}
