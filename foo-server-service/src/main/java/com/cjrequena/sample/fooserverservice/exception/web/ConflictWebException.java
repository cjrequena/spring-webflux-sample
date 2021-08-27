package com.cjrequena.sample.fooserverservice.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class ConflictWebException extends WebException {
  public ConflictWebException() {
    super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase());
  }

  public ConflictWebException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
