package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;
import lombok.Getter;

/** Generic Exception. */
@Getter
public class GenericException extends RuntimeException {

  private final GenericError genericError;

  public GenericException(GenericError genericError) {
    super(genericError.getDescription());
    this.genericError = genericError;
  }

  public GenericException(GenericError genericError, String args) {
    super(genericError.getDescription() + " - " + args);
    this.genericError = genericError;
  }
}
