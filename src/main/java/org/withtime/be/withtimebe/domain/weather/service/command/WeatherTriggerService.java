package org.withtime.be.withtimebe.domain.weather.service.command;

import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherSyncReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

public interface WeatherTriggerService {

    WeatherSyncResDTO.ManualTriggerResult triggerAsync(WeatherSyncReqDTO.ManualTrigger request);
}
