package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;
import lombok.Getter;

/**
 * Duplicate Entity Exception.
 */
@Getter
public class DuplicateEntityException extends GenericException {

  public DuplicateEntityException(GenericError genericError) {
    super(genericError);
  }
}
