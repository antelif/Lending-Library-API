package com.antelif.library.application.exception;

import static com.antelif.library.application.error.GenericError.GENERIC_ERROR;
import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Rest Controller Exception Handler. */
@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

  /***
   * Handles all exceptions thrown by the system due to internal errors.
   * @return an Error Response object of Generic Error.
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public ErrorResponse exceptionHandler(Exception exception) {
    var error =
        new ErrorResponse(GENERIC_ERROR, INTERNAL_SERVER_ERROR.value(), exception.getMessage());

    log.error(error.toString());

    return error;
  }
  /***
   * Handles all custom exceptions that extend GenericException.
   * @param exception the exception that was thrown.
   * @return an Error Response object with information about the error that occurred.
   */
  @ExceptionHandler(value = GenericException.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public ErrorResponse genericExceptionHandler(GenericException exception) {
    var error =
        new ErrorResponse(
            exception.getGenericError(), INTERNAL_SERVER_ERROR.value(), exception.getMessage());

    log.error(error.toString());

    return error;
  }

  /**
   * Handles MethodArgumentNotValidException that occurs during input validations.
   *
   * @param exception the exception that was thrown.
   * @return an Error Response object with information about the error that occurred.
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public ErrorResponse methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException exception) {
    var error =
        new ErrorResponse(
            INPUT_VALIDATIONS_ERROR,
            INTERNAL_SERVER_ERROR.value(),
            exception.getFieldError().getDefaultMessage());

    log.error(error.toString());

    return error;
  }
}
