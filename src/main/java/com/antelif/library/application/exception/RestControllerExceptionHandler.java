package com.antelif.library.application.exception;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.exception.GenericException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Rest Controller Exception Handler. */
@RestControllerAdvice
public class RestControllerExceptionHandler {

  /***
   * Handles all exceptions that extend GenericException.
   * @param exception the exception that was thrown.
   * @return an Error Response object with information about the error that occurred.
   */
  @ExceptionHandler(value = GenericException.class)
  public ErrorResponse handlerDuplicateEntityException(GenericException exception) {

    return new ErrorResponse(exception.getGenericError());
  }
}
