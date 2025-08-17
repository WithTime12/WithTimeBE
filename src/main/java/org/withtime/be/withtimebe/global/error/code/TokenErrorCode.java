package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN401_1", "토큰의 기한이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401_2", "리프레시 토큰이 유효하지 않습니다."),
    NOT_EXISTS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN400_1", "토큰이 존재하지 않습니다."),
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
