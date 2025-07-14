package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class OAuthException extends ServerApplicationException {
    public OAuthException(BaseErrorCode code) {
        super(code);
    }
}
