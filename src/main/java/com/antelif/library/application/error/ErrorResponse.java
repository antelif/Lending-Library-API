package com.antelif.library.application.error;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Contains all information to display when an error occurs. */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ErrorResponse {

  private String name;
  private int code;
  private String description;
  private int status;
  private Instant timestamp;

  /**
   * Constructor for custom exceptions.
   *
   * @param genericError the error that occurred,
   * @param status the http request status of the error that occurred.
   */
  public ErrorResponse(GenericError genericError, int status) {
    this.name = genericError.getName();
    this.code = genericError.getCode();
    this.description = genericError.getDescription();
    this.status = status;
    this.timestamp = Instant.now();
  }

  /**
   * Constructor for validation exceptions.
   *
   * @param genericError the error that occurred,
   * @param status the http request status of the error that occurred,
   * @param description the description provided by the runtime exception that occurred.
   */
  public ErrorResponse(GenericError genericError, int status, String description) {
    this.name = genericError.getName();
    this.code = genericError.getCode();
    this.description = description;
    this.status = status;
    this.timestamp = Instant.now();
  }
}
