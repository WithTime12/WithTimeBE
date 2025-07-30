package org.withtime.be.withtimebe.domain.member.alarm.generator;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface AlarmMessageGenerator<T> {
    T generate(Member member, AlarmRequestDTO.SendAlarm request);
}
