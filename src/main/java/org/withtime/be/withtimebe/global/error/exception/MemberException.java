package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class MemberException extends ServerApplicationException {

    public MemberException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
