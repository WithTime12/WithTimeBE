package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.ErrorReasonDTO;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum DateCourseErrorCode implements BaseErrorCode {
    // 데이트 코스 에러
    DateCourse_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE_COURSE404_1", "해당하는 데이트 코스를 찾을 수 없습니다."),
    // 데이트 코스 입력 에러
    DateCourse_INVALID_INPUT(HttpStatus.BAD_REQUEST, "DATE_COURSE400_1", "유효하지 않은 입력값입니다"),
    // 데이트 코스 예산 범위에 맞지 않다는 에러를 ENUM을 보여줘
    DateCourse_INVALID_BUDGET_RANGE(HttpStatus.BAD_REQUEST, "DATE_COURSE400_2", "데이트 코스 예산 범위가 올바르지 않습니다"),

    // 데이트 코스 북마크 에러
    DateCourseBookMark_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE_COURSE_BOOKMARK404_1", "해당하는 북마크된 데이트 코스를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return DefaultResponseErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .code(this.code)
                .message(this.message)
                .build();
    }

}
