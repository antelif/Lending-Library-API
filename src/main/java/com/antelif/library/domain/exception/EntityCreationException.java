package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

/** Entity Creation Exception. */
public class EntityCreationException extends GenericException {

  /** Constructor. */
  public EntityCreationException(GenericError genericError) {
    super(genericError);
  }
}
