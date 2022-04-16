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
}
