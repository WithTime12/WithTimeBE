package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public class AlarmConverter {
    public static Alarm toAlarm(Member member, AlarmRequestDTO.SendAlarm request) {
        return Alarm.builder()
                .title(request.title())
                .description(request.description())
                .alarmType(request.alarmType())
                .member(member)
                .isRead(false)
                .build();
    }

    public static AlarmResponseDTO.UpdateSetting toUpdateSetting(Member member) {
        return AlarmResponseDTO.UpdateSetting.builder()
                .pushAlarm(member.getPushAlarm())
                .emailAlarm(member.getEmailAlarm())
                .smsAlarm(member.getSmsAlarm())
                .build();
    }

    public static AlarmResponseDTO.SettingInfo toSettingInfo(Member member) {
        return AlarmResponseDTO.SettingInfo.builder()
                .pushAlarm(member.getPushAlarm())
                .emailAlarm(member.getEmailAlarm())
                .smsAlarm(member.getSmsAlarm())
                .build();
    }
}
