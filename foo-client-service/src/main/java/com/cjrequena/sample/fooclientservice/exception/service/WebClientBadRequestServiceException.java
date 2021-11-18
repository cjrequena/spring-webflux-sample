package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class WebClientBadRequestServiceException extends ServiceException {
  public WebClientBadRequestServiceException() {
    super(EErrorMessage.FEIGN_BAD_REQUEST_EXCEPTION.getMessage());
  }

  public WebClientBadRequestServiceException(String message) {
    super(message);
  }

  public WebClientBadRequestServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
