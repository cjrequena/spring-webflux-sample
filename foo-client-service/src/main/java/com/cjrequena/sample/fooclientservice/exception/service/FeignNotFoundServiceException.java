package com.cjrequena.sample.fooclientservice.exception.service;

import com.cjrequena.sample.fooclientservice.exception.EErrorMessage;
import org.springframework.lang.Nullable;

public class FeignNotFoundServiceException extends ServiceException {
  public FeignNotFoundServiceException() {
    super(EErrorMessage.FEIGN_NOT_FOUND_EXCEPTION.getMessage());
  }

  public FeignNotFoundServiceException(String message) {
    super(message);
  }

  public FeignNotFoundServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
