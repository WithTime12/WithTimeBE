package org.withtime.be.withtimebe.domain.member.alarm.sender;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Component
@RequiredArgsConstructor
public class EmailSMTPAlarmSendUtil implements AlarmSendUtil<MimeMessage> {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(Member member, MimeMessage message) throws Exception {
        javaMailSender.send(message);
    }
}
