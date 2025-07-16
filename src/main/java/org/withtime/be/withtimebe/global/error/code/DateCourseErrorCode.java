package org.withtime.be.withtimebe.global.error.code;

import lombok.AllArgsConstructor;
import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.ErrorReasonDTO;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum DateCourseErrorCode implements BaseErrorCode {
    DateCourse_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE_COURSE404_1", "해당하는 데이트 코스를 찾을 수 없습니다."),
    DateCourseBookMark_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE_COURSE_BOOKMARK404_1", "해당하는 북마크된 데이트 코스를 찾을 수 없습니다.");

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
