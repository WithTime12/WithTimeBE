package org.withtime.be.withtimebe.domain.weather.service.query;

import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;

public interface RegionQueryService {

    RegionResDTO.RegionCodeList getAllRegionCodes();
}
