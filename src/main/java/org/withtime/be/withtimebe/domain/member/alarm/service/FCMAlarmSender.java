package org.withtime.be.withtimebe.domain.member.alarm.service;

import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.member.alarm.generator.AlarmMessageGenerator;
import org.withtime.be.withtimebe.domain.member.alarm.sender.AlarmSendUtil;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMAlarmSender implements AlarmSender<Message> {

    private final AlarmMessageGenerator<Message> alarmMessageGenerator;
    private final AlarmSendUtil<Message> alarmSendUtil;

    @Override
    public void send(Member member, AlarmRequestDTO.SendAlarm request) throws Exception {
        try {
            Message message = alarmMessageGenerator.generate(member, request);

            alarmSendUtil.send(member, message);
        } catch (Exception e) {
            log.warn("FCM Alarm error", e);
            throw e;
        }
    }

}
