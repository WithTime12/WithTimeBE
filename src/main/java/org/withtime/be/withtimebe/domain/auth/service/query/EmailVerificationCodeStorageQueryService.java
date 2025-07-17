package org.withtime.be.withtimebe.domain.auth.service.query;

public interface EmailVerificationCodeStorageQueryService {
    boolean checkVerificationCode(String email, String verificationCode);
    boolean isVerified(String email);
}
