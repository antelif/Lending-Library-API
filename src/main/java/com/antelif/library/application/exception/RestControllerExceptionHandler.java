package com.antelif.library.application.exception;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.exception.GenericException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Rest Controller Exception Handler. */
@RestControllerAdvice
public class RestControllerExceptionHandler {

  /***
   * Handles all custom exceptions that extend GenericException.
   * @param exception the exception that was thrown.
   * @return an Error Response object with information about the error that occurred.
   */
  @ExceptionHandler(value = GenericException.class)
  public ErrorResponse duplicateEntityExceptionHandler(GenericException exception) {

    return new ErrorResponse(exception.getGenericError());
  }

  /**
   * Handles MethodArgumentNotValidException that occurs during input validations.
   *
   * @param exception the exception that was thrown.
   * @return an Error Response object with information about the error that occurred.
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ErrorResponse methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException exception) {
    return new ErrorResponse(
        INPUT_VALIDATIONS_ERROR, exception.getFieldError().getDefaultMessage());
  }
}
