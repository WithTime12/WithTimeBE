package org.withtime.be.withtimebe.domain.member.dto;

import lombok.Builder;

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

}
