package com.antelif.library.application.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Contains all information to display when an error occurs. */
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

  private String name;
  private int code;
  private String description;
  private String args;

  /**
   * Constructor.
   *
   * @param genericError the error that occurred
   */
  public ErrorResponse(GenericError genericError) {
    this.name = genericError.getName();
    this.code = genericError.getCode();
    this.description = genericError.getDescription();
  }

  /**
   * Constructor.
   *
   * @param genericError the error that occurred
   * @param args error arguments
   */
  public ErrorResponse(GenericError genericError, String args) {
    this.name = genericError.getName();
    this.code = genericError.getCode();
    this.description = genericError.getDescription();
    this.args = args;
  }
}
