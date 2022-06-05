package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;
import com.antelif.library.infrastructure.entity.BookCopyEntity;

/** Book Copy Is Unavailable exception. */
public class BookCopyIsUnavailableException extends GenericException {

  /**
   * Constructor.
   *
   * @param genericError the error tha occurred,
   * @param copy the book copy entity object.
   */
  public BookCopyIsUnavailableException(GenericError genericError, BookCopyEntity copy) {
    super(genericError, copy.toString());
  }
}
