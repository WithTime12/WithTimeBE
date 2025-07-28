package org.withtime.be.withtimebe.domain.member.alarm.service;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface AlarmSender<T> {

    void send(Member member, AlarmRequestDTO.SendAlarm request) throws Exception;
}
