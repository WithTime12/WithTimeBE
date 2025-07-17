package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum EmailErrorCode implements BaseErrorCode {

    FAIL_EMAIL_SEND(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL500_1", "이메일 전송에 실패했습니다."),
    INCORRECT_EMAIL_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "EMAIL401_1", "이메일 인증에 실패했습니다."),
    UNVERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "EMAIL401_2", "인증되지 않은 이메일이거나 인증 유효기간이 지났습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public DefaultResponseErrorReasonDTO getReason() {
        return DefaultResponseErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
