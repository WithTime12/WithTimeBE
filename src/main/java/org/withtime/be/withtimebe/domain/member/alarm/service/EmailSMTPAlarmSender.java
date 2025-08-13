package org.withtime.be.withtimebe.domain.member.alarm.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.alarm.generator.AlarmMessageGenerator;
import org.withtime.be.withtimebe.domain.member.alarm.sender.AlarmSendUtil;

@Component
public class EmailSMTPAlarmSender extends AbstractAlarmSender<MimeMessage> {

    private static final Class<MimeMessage> SUPPORTED_CLASS = MimeMessage.class;

    public EmailSMTPAlarmSender(AlarmMessageGenerator<MimeMessage> alarmMessageGenerator,
                                AlarmSendUtil<MimeMessage> alarmSendUtil) {
        super(alarmMessageGenerator, alarmSendUtil);
    }

    @Override
    public Class<MimeMessage> supportedClass() {
        return SUPPORTED_CLASS;
    }
}
