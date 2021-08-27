package com.cjrequena.sample.fooclientservice.exception;

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
public enum EErrorMessage {

  FEIGN_CONFLICT_EXCEPTION("Entity already exists"),
  FEIGN_NOT_FOUND_EXCEPTION("Entity was not found"),
  FEIGN_BAD_REQUEST_EXCEPTION("Query is malformed");

  private String message;

  /**
   *
   * @param message
   */
  EErrorMessage(String message) {
    this.message = message;
  }

  /**
   *
   * @return
   */
  public String getMessage() {
    return this.message;
  }

}


