package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

/** Entity Does Not Exist Exception. */
public class EntityDoesNotExistException extends GenericException {

  public EntityDoesNotExistException(GenericError genericError) {
    super(genericError);
  }
}
