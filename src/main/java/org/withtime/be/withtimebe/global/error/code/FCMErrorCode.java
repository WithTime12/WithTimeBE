package org.withtime.be.withtimebe.global.error.code;

import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FCMErrorCode implements BaseErrorCode {
    FIREBASE_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE500_1", "알림 메시지를 보내는 데 실패했습니다."),
    UNKNOWN_FIREBASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE500_2", "알 수 없는 알림 메시지 에러입니다.")
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
