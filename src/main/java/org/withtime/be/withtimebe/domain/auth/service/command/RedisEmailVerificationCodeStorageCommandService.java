package org.withtime.be.withtimebe.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.constants.EmailVerificationStorageConstants;
import org.withtime.be.withtimebe.global.util.RedisUtil;

@Service
@RequiredArgsConstructor
public class RedisEmailVerificationCodeStorageCommandService implements EmailVerificationCodeStorageCommandService {

    private final RedisUtil redisUtil;

    @Override
    public void saveVerificationCode(String email, String verificationCode) {
        redisUtil.set(EmailVerificationStorageConstants.VERIFICATION_CODE_PREFIX + email, verificationCode, EmailVerificationStorageConstants.VERIFICATION_CODE_DURATION);
    }

    @Override
    public void saveVerifiedEmail(String email) {
        redisUtil.set(EmailVerificationStorageConstants.EMAIL_VERIFICATION_PREFIX + email, true, EmailVerificationStorageConstants.EMAIL_VERIFICATION_DURATION);
    }

    @Override
    public void deleteVerificationCode(String email) {
        redisUtil.delete(EmailVerificationStorageConstants.VERIFICATION_CODE_PREFIX + email);
    }

    @Override
    public void deleteVerifiedEmail(String email) {
        redisUtil.delete(EmailVerificationStorageConstants.EMAIL_VERIFICATION_PREFIX + email);
    }
}
