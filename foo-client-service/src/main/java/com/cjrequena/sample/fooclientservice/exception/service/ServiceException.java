package com.cjrequena.sample.fooclientservice.exception.service;

import lombok.ToString;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@ToString
public class ServiceException extends Exception {

  /**
   * @param exception - The exception
   */
  public ServiceException(Throwable exception) {
    super(exception);
  }

  /**
   * @param message - The error exceptionMessage
   */
  public ServiceException(String message) {
    super(message);
  }

  /**
   *
   * @param message - The error exceptionMessage
   * @param tex - Throwable exception
   */
  public ServiceException(String message, Throwable tex) {
    super(message, tex);
  }

}
