package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RegionErrorCode implements BaseErrorCode {

    // ==== 지역 관련 에러 (404) ====
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION404_0", "지역을 찾을 수 없습니다."),
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
