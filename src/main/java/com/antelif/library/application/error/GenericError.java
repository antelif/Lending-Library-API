package com.antelif.library.application.error;

import lombok.Getter;

@Getter
/** Errors. */
public enum GenericError {
  DUPLICATE_AUTHOR(1, "Author already exists."),
  AUTHOR_DOES_NOT_EXIST(2, "Author does not exist."),
  AUTHOR_CONVERTER_FAILED(3, "Could not convert author object."),
  AUTHOR_CREATION_FAILED(4, "Could not create new author.");

  private final int code;
  private final String description;

  /**
   * Constructor.
   *
   * @param code the error code,
   * @param description the error description
   */
  GenericError(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /** Get the name of the GenericError. */
  public String getName() {
    return this.name();
  }
}
