package org.withtime.be.withtimebe.domain.auth.util;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.withtime.be.withtimebe.global.error.code.EmailErrorCode;
import org.withtime.be.withtimebe.global.error.exception.EmailException;

@Component
@RequiredArgsConstructor
public class SMTPMailVerificationCodeSender implements MailVerificationCodeSender {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(String toEmail, String code) {

        // 1. 템플릿 처리
        Context context = new Context();
        context.setVariable("code", code);
        String html = templateEngine.process("email-verification", context);

        // 2. 메일 작성 및 전송
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("[WithTime] 이메일 인증 코드입니다.");
            helper.setText(html, true); // true → HTML 형식

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException(EmailErrorCode.FAIL_EMAIL_SEND);
        }
    }
}
