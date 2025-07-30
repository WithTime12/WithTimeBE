package org.withtime.be.withtimebe.domain.member.alarm.service;

import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.alarm.generator.AlarmMessageGenerator;
import org.withtime.be.withtimebe.domain.member.alarm.sender.AlarmSendUtil;

@Component
public class FCMAlarmSender extends AbstractAlarmSender<Message> {

    private static final Class<Message> SUPPORTED_CLASS= Message.class;

    public FCMAlarmSender(AlarmMessageGenerator<Message> alarmMessageGenerator,
                          AlarmSendUtil<Message> alarmSendUtil) {
        super(alarmMessageGenerator, alarmSendUtil);
    }

    @Override
    public Class<Message> supportedClass() {
        return SUPPORTED_CLASS;
    }
}
