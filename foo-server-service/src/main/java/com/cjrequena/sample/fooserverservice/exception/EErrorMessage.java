package com.cjrequena.sample.fooserverservice.exception;

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

  DATABASE_CONFLICT_EXCEPTION("The database entity already exists"),
  DATABASE_NOT_FOUND_EXCEPTION("The database entity was not found"),
  DATABASE_BAD_REQUEST_EXCEPTION("The database query is malformed"),
  DATABASE_CONSTRAINT_VIOLATION_EXCEPTION("Database constraint violation");

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


