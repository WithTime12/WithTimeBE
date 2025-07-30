package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmErrorCode implements BaseErrorCode {

    NOT_FOUND_ALARM_SENDER(HttpStatus.INTERNAL_SERVER_ERROR, "ALARM500_1", "알림을 보내는 장치가 없습니다."),
    ALARM_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR," ALARM500_2", "알림 보내기에 실패했습니다.")
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
