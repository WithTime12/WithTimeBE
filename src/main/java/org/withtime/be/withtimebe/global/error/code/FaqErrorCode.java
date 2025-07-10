package org.withtime.be.withtimebe.global.error.code;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FaqErrorCode implements BaseErrorCode {

	FAQ_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ404_1", "해당하는 질문 유형을 찾을 수 없습니다."),
	FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ404_2", "해당하는 질문을 찾을 수 없습니다."),
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
