package org.withtime.be.withtimebe.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.constants.EmailVerificationStorageConstants;
import org.withtime.be.withtimebe.global.util.RedisUtil;

@Service
@RequiredArgsConstructor
public class RedisEmailVerificationCodeStorageQueryService implements EmailVerificationCodeStorageQueryService {

    private final RedisUtil redisUtil;

    @Override
    public boolean checkVerificationCode(String email, String verificationCode) {
        return redisUtil.get(EmailVerificationStorageConstants.VERIFICATION_CODE_PREFIX, String.class).equals(verificationCode);
    }

    @Override
    public boolean isVerified(String email) {
        return redisUtil.has(EmailVerificationStorageConstants.EMAIL_VERIFICATION_PREFIX + email);
    }
}
