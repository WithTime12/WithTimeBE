package org.withtime.be.withtimebe.domain.member.service;

import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;

public interface AlarmQueryService {
    AlarmResponseDTO.FindAlarmList findAlarms(Long cursor, Integer size);
}
