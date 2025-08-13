package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class GradeException extends ServerApplicationException {
	public GradeException(BaseErrorCode code) {
		super(code);
	}
}
