package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class FeignBadRequestServiceException extends ServiceException {
  public FeignBadRequestServiceException() {
    super(EErrorMessage.FEIGN_BAD_REQUEST_EXCEPTION.getMessage());
  }

  public FeignBadRequestServiceException(String message) {
    super(message);
  }

  public FeignBadRequestServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
