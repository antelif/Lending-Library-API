package com.antelif.library.application.error;

import lombok.Getter;

/** Errors. */
@Getter
public enum GenericError {
  DUPLICATE_AUTHOR(1, "Author already exists."),
  AUTHOR_DOES_NOT_EXIST(2, "Author does not exist."),
  AUTHOR_CONVERTER_FAILED(3, "Could not convert author object."),
  AUTHOR_CREATION_FAILED(4, "Could not create new author."),

  DUPLICATE_PUBLISHER(5, "Publisher already exists."),
  PUBLISHER_DOES_NOT_EXIST(6, "Publisher does not exist."),
  PUBLISHER_CONVERTER_FAILED(7, "Could not convert publisher object."),
  PUBLISHER_CREATION_FAILED(8, "Could not create new publisher."),

  DUPLICATE_BOOK(9, "Book already exists"),
  BOOK_CREATION_FAILED(10, "Could not create new author."),

  DUPLICATE_PERSONNEL(13, "Personnel already exists."),
  PERSONNEL_DOES_NOT_EXIST(14, "Personnel does not exist."),
  PERSONNEL_CONVERTER_FAILED(15, "Could not convert personnel object."),
  PERSONNEL_CREATION_FAILED(16, "Could not create new personnel.");

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
