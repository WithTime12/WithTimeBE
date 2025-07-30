package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class FCMException extends ServerApplicationException {

    public FCMException(BaseErrorCode code) {
        super(code);
    }
}
