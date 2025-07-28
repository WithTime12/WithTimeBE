package org.withtime.be.withtimebe.domain.member.dto;

import org.withtime.be.withtimebe.domain.member.entity.enums.AlarmType;

public record AlarmRequestDTO() {
    public record SendAlarm(
            String title,
            String description,
            AlarmType alarmType
    ) {

    }

    public record UpdateDeviceToken(
            String deviceToken
    ) {

    }

    public record UpdateSetting(
            Boolean emailAlarm,
            Boolean pushAlarm,
            Boolean smsAlarm
    ) {

    }
}
