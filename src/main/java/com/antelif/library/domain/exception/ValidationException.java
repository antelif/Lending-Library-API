package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

/**
 * Validation exception that occurs during validations.
 */
public class ValidationException extends GenericException {

  /**
   * Constructor.
   */
  public ValidationException(GenericError genericError, String args) {
    super(genericError, args);
  }

  /**
   * Constructor.
   */
  public ValidationException(GenericError genericError) {
    super(genericError);
  }
}
