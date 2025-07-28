package org.withtime.be.withtimebe.domain.member.dto;

import org.withtime.be.withtimebe.domain.member.entity.enums.AlarmType;

public record AlarmRequestDTO() {
    public record SendAlarm(
            String title,
            String description,
            AlarmType alarmType
    ) {

    }
}
