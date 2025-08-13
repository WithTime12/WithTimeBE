package org.withtime.be.withtimebe.domain.member.converter;

import org.springframework.data.domain.Slice;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.util.List;

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

    public static AlarmResponseDTO.FindAlarmList toFindAlarmList(Slice<Alarm> alarmSlice) {
        List<Alarm> alarms = alarmSlice.getContent();
        return AlarmResponseDTO.FindAlarmList.builder()
                .alarmList(alarms.stream().map(AlarmConverter::toFindAlarmListItem).toList())
                .cursor(alarmSlice.hasNext() ? alarms.get(alarms.size() - 1).getId() : null)
                .hasNext(alarmSlice.hasNext())
                .size(alarmSlice.getNumberOfElements())
                .build();
    }

    public static AlarmResponseDTO.FindAlarmListItem toFindAlarmListItem(Alarm alarm) {
        return AlarmResponseDTO.FindAlarmListItem.builder()
                .id(alarm.getId())
                .title(alarm.getTitle())
                .alarmType(alarm.getAlarmType())
                .isRead(alarm.getIsRead())
                .build();
    }
}
