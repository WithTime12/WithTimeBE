package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class DateCourseException extends ServerApplicationException {

    public DateCourseException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }

}
