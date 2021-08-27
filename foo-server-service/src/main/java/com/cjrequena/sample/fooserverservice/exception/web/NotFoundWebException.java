package com.cjrequena.sample.fooserverservice.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class NotFoundWebException extends WebException {
  public NotFoundWebException() {
    super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
  }

  public NotFoundWebException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
