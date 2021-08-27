package com.cjrequena.sample.fooserverservice.exception.service;

import com.cjrequena.sample.fooserverservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class DBConstraintViolationServiceException extends ServiceException {
  public DBConstraintViolationServiceException() {
    super(EErrorMessage.DATABASE_CONSTRAINT_VIOLATION_EXCEPTION.getMessage());
  }

  public DBConstraintViolationServiceException(String message) {
    super(message);
  }

  public DBConstraintViolationServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
