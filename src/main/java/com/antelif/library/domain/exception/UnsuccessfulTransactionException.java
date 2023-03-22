package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

/**
 * Unsuccessful transaction exception used in validations.
 */
public class UnsuccessfulTransactionException extends GenericException {

  /**
   * Constructor.
   */
  public UnsuccessfulTransactionException(GenericError error) {
    super(error);
  }
}
