package org.withtime.be.withtimebe.domain.member.alarm.generator;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Component
@RequiredArgsConstructor
public class EmailSMTPAlarmMessageGenerator implements AlarmMessageGenerator<MimeMessage> {

    private static final String TITLE_FORMAT = "[WithTime] %s: %s";
    private final JavaMailSender javaMailSender;

    @Override
    public MimeMessage generate(Member member, AlarmRequestDTO.SendAlarm request) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(member.getEmail());
            helper.setSubject(String.format(TITLE_FORMAT, request.alarmType(), request.title()));
            helper.setText(request.description());

            return mimeMessage;
        } catch (Exception e) {
            return null;
        }
    }

}
