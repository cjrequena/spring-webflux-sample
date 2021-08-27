package com.cjrequena.sample.fooserverservice.exception.service;

import com.cjrequena.sample.fooserverservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class DBConflictServiceException extends ServiceException {
  public DBConflictServiceException() {
    super(EErrorMessage.DATABASE_CONFLICT_EXCEPTION.getMessage());
  }

  public DBConflictServiceException(String message) {
    super(message);
  }

  public DBConflictServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }

}
