package org.withtime.be.withtimebe.domain.auth.service.command;

public interface EmailVerificationCodeStorageCommandService {
    /**
     * 인증 코드 저장
     * @param email 인증 코드를 확인할 이메일
     * @param verificationCode 이메일에 대한 인증 코드
     */
    void saveVerificationCode(String email, String verificationCode);

    /**
     * 이메일에 대한 인증이 완료됨을 저장
     * @param email 인증이 완료됨을 저장할 이메일
     */
    void saveVerifiedEmail(String email);

    /**
     * 이메일에 대한 인증 코드 삭제
     * @param email 인증 코드를 삭제할 이메일
     */
    void deleteVerificationCode(String email);

    /**
     * 인증 정보 삭제
     * @param email 인증 정보를 삭제할 이메일
     */
    void deleteVerifiedEmail(String email);
}
