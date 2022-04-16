package com.antelif.library.application.error;

import lombok.Getter;

@Getter
public enum GenericError {
  DUPLICATE_AUTHOR(1, "Author already exists."),
  AUTHOR_DOES_NOT_EXIST(2, "Author does not exist."),
  AUTHOR_CONVERTER_FAILED(3, "Could not convert author object.");

  private final int code;
  private final String description;

  GenericError(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getName() {
    return this.name();
  }
}