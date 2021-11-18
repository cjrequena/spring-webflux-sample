package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class WebClientNotFoundServiceException extends ServiceException {
  public WebClientNotFoundServiceException() {
    super(EErrorMessage.FEIGN_NOT_FOUND_EXCEPTION.getMessage());
  }

  public WebClientNotFoundServiceException(String message) {
    super(message);
  }

  public WebClientNotFoundServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
