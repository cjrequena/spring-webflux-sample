package com.cjrequena.sample.fooserverservice.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class BadRequestWebException extends WebException {
  public BadRequestWebException() {
    super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
  }

  public BadRequestWebException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
