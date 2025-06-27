package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class AuthException extends ServerApplicationException {

    public AuthException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
