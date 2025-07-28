package org.withtime.be.withtimebe.domain.member.service;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface AlarmCommandService {
    void send(Member member, AlarmRequestDTO.SendAlarm... request);
    void updateDeviceToken(Member member, AlarmRequestDTO.UpdateDeviceToken request);
    AlarmResponseDTO.UpdateSetting updateAlarmSetting(Member member, AlarmRequestDTO.UpdateSetting request);
}
