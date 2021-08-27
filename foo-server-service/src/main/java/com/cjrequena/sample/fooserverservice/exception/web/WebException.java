package com.cjrequena.sample.fooserverservice.exception.web;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@ToString
public abstract class WebException extends Exception {

  @Getter
  private final HttpStatus httpStatus;

  /**
   *
   * @param httpStatus
   */
  public WebException(HttpStatus httpStatus) {
    super(httpStatus.getReasonPhrase());
    this.httpStatus = httpStatus;
  }

  /**
   *
   * @param httpStatus
   * @param message
   */
  public WebException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  /**
   *
   * @param httpStatus
   * @param throwable
   */
  public WebException(HttpStatus httpStatus, Throwable throwable) {
    super(httpStatus.getReasonPhrase(), throwable);
    this.httpStatus = httpStatus;
  }

  /**
   *
   * @param httpStatus
   * @param message
   * @param throwable
   */
  public WebException(HttpStatus httpStatus, String message, Throwable throwable) {
    super(message, throwable);
    this.httpStatus = httpStatus;
  }

}
