package org.withtime.be.withtimebe.domain.member.dto;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.member.entity.enums.AlarmType;

import java.util.List;

public record AlarmResponseDTO() {
    @Builder
    public record UpdateSetting(
            Boolean emailAlarm,
            Boolean pushAlarm,
            Boolean smsAlarm
    ) {

    }

    @Builder
    public record SettingInfo(
            Boolean emailAlarm,
            Boolean pushAlarm,
            Boolean smsAlarm
    ) {

    }

    @Builder
    public record FindAlarmList(
            List<FindAlarmListItem> alarmList,
            Integer size,
            Boolean hasNext,
            Long cursor
    ) {

    }

    @Builder
    public record FindAlarmListItem(
            Long id,
            String title,
            AlarmType alarmType,
            boolean isRead
    ) {

    }
}
