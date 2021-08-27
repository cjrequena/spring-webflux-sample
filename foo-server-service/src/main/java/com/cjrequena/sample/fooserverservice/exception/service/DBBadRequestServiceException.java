package com.cjrequena.sample.fooserverservice.exception.service;

import com.cjrequena.sample.fooserverservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class DBBadRequestServiceException extends ServiceException {
  public DBBadRequestServiceException() {
    super(EErrorMessage.DATABASE_BAD_REQUEST_EXCEPTION.getMessage());
  }

  public DBBadRequestServiceException(String message) {
    super(message);
  }

  public DBBadRequestServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
