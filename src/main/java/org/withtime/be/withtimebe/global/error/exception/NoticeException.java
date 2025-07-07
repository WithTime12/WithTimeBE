package org.withtime.be.withtimebe.global.error.exception;

import org.namul.api.payload.code.BaseErrorCode;
import org.namul.api.payload.error.exception.ServerApplicationException;

public class NoticeException extends ServerApplicationException {
  public NoticeException(BaseErrorCode baseErrorCode) {
    super(baseErrorCode);
  }
}
