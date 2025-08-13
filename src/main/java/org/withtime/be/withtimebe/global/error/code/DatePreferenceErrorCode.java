package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum DatePreferenceErrorCode implements BaseErrorCode {

    INVALID_ANSWERS(HttpStatus.BAD_REQUEST, "DATE_PREFERENCE400_1", "질문에 대한 응답의 형식이 잘못되었습니다."),
    NOT_FOUND_DESCRIPTION(HttpStatus.BAD_REQUEST, "DATE_PREFERENCT404_1", "데이트 유형에 대한 설명을 찾지 못했습니다."),
    NOT_FOUND_RELATION(HttpStatus.NOT_FOUND, "DATE_PREFERENCE404_2", "잘 맞는 혹은 안 맞는 유형 정보가 없습니다.")
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
