package org.withtime.be.withtimebe.domain.member.alarm.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.withtime.be.withtimebe.domain.member.alarm.generator.AlarmMessageGenerator;
import org.withtime.be.withtimebe.domain.member.alarm.sender.AlarmSendUtil;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAlarmSender<T> implements AlarmSender<T> {

    private final AlarmMessageGenerator<T> alarmMessageGenerator;
    private final AlarmSendUtil<T> alarmSendUtil;

    @Override
    public void send(Member member, AlarmRequestDTO.SendAlarm request) throws Exception {
        try {
            T message = alarmMessageGenerator.generate(member, request);

            alarmSendUtil.send(member, message);
        } catch (Exception e) {
            handleException(e);
        }
    }

    protected void handleException(Exception e) throws Exception{
        log.warn("Alarm error", e);
        throw e;
    }
}
