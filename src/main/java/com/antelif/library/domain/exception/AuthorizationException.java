package com.antelif.library.domain.exception;

import static com.antelif.library.application.error.GenericError.AUTHORIZATION_FAILED;

/** Personnel Authorization Exception. */
public class AuthorizationException extends GenericException {

  public AuthorizationException() {
    super(AUTHORIZATION_FAILED);
  }
}
