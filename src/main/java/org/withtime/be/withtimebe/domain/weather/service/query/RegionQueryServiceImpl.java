package org.withtime.be.withtimebe.domain.weather.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.weather.converter.RegionConverter;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.repository.RegionCodeRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionQueryServiceImpl implements RegionQueryService{

    private final RegionCodeRepository regionCodeRepository;
    private final RegionRepository regionRepository;

    @Override
    public RegionResDTO.RegionCodeList getAllRegionCodes() {
        List<Object[]> regionCodesWithCount = regionCodeRepository.findAllWithRegionCount();
        return RegionConverter.toRegionCodeList(regionCodesWithCount);
    }

    @Override
    public RegionResDTO.RegionList getAllRegions() {
        List<Region> regions = regionRepository.findAllActiveRegions();
        return RegionConverter.toRegionList(regions);
    }

}
