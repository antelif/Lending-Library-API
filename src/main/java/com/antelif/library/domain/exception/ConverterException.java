package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;

/** Converter Exception. */
public class ConverterException extends GenericException {

  public ConverterException(GenericError genericError) {
    super(genericError);
  }
}
