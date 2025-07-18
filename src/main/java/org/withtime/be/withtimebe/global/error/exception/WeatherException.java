package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;

public class WeatherException extends ServerApplicationException {
    public WeatherException(BaseErrorCode baseErrorCode)  {
        super(baseErrorCode);
    }
}
