package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
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
}
