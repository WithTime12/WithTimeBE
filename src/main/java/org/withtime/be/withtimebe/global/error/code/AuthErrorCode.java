package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    FAIL_AUTH_LOGIN(HttpStatus.UNAUTHORIZED, "AUTH401_1", "일반 로그인에 실패했습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "AUTH400_1", "이미 존재하는 이메일입니다.")
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
