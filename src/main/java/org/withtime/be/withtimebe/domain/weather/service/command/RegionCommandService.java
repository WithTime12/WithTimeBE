package org.withtime.be.withtimebe.domain.weather.service.command;

import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;

public interface RegionCommandService {

    RegionResDTO.CreateRegionCode createRegionCode(RegionReqDTO.CreateRegionCode request);

    RegionResDTO.CreateRegion createRegion(RegionReqDTO.CreateRegion request);
}
