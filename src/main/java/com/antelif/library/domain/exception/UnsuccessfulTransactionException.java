package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

public class UnsuccessfulTransactionException extends GenericException {

  public UnsuccessfulTransactionException(GenericError error) {
    super(error);
  }

  public UnsuccessfulTransactionException(GenericError error, String args) {
    super(error, args);
  }
}
