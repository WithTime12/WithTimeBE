package org.withtime.be.withtimebe.domain.auth.util;

public interface MailVerificationCodeSender {
    void sendMail(String toEmail, String code);
}
