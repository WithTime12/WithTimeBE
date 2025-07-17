package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class EmailException extends ServerApplicationException {
    public EmailException(BaseErrorCode code) {
        super(code);
    }
}
