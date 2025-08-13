package org.withtime.be.withtimebe.global.error.code;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GradeErrorCode implements BaseErrorCode {

	GRADE_NOT_FOUND(HttpStatus.NOT_FOUND, "GRADE404_1", "등급을 찾지 못했습니다."),
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
