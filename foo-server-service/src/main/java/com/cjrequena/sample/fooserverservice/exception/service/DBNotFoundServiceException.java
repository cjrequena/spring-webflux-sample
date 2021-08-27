package com.cjrequena.sample.fooserverservice.exception.service;

import com.cjrequena.sample.fooserverservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class DBNotFoundServiceException extends ServiceException {

  public DBNotFoundServiceException() {
    super(EErrorMessage.DATABASE_NOT_FOUND_EXCEPTION.getMessage());
  }

  public DBNotFoundServiceException(String message) {
    super(message);
  }

  public DBNotFoundServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
