package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class WebClientConflictServiceException extends ServiceException {
  public WebClientConflictServiceException() {
    super(EErrorMessage.FEIGN_CONFLICT_EXCEPTION.getMessage());
  }

  public WebClientConflictServiceException(String message) {
    super(message);
  }

  public WebClientConflictServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
