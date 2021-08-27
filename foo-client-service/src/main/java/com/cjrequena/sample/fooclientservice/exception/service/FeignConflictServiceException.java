package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class FeignConflictServiceException extends ServiceException {
  public FeignConflictServiceException() {
    super(EErrorMessage.FEIGN_CONFLICT_EXCEPTION.getMessage());
  }

  public FeignConflictServiceException(String message) {
    super(message);
  }

  public FeignConflictServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
