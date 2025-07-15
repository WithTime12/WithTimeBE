package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class FaqException extends ServerApplicationException {

	public FaqException(BaseErrorCode baseErrorCode) {
		super(baseErrorCode);
	}
}
