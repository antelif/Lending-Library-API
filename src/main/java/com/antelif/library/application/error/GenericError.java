package com.antelif.library.application.error;

import lombok.Getter;

/** Errors. */
@Getter
public enum GenericError {
  DUPLICATE_AUTHOR(1, "Author already exists."),
  AUTHOR_DOES_NOT_EXIST(2, "Author does not exist."),
  AUTHOR_CREATION_FAILED(4, "Could not create new author."),

  DUPLICATE_PUBLISHER(5, "Publisher already exists."),
  PUBLISHER_DOES_NOT_EXIST(6, "Publisher does not exist."),
  PUBLISHER_CREATION_FAILED(8, "Could not create new publisher."),

  DUPLICATE_BOOK(9, "Book already exists"),
  BOOK_DOES_NOT_EXIST(10, "Book does not exist."),
  BOOK_CREATION_FAILED(12, "Could not create new book."),

  DUPLICATE_PERSONNEL(13, "Personnel already exists."),
  PERSONNEL_DOES_NOT_EXIST(14, "Personnel does not exist."),
  PERSONNEL_CREATION_FAILED(16, "Could not create new personnel."),

  DUPLICATE_CUSTOMER(17, "Customer already exists."),
  CUSTOMER_DOES_NOT_EXIST(18, "Customer does not exist."),
  CUSTOMER_CREATION_FAILED(20, "Could not create new customer."),

  BOOK_COPY_DOES_NOT_EXIST(22, "Book copy does not exist."),
  BOOK_COPY_CREATION_FAILED(24, "Could not create new book copy."),
  BOOK_COPY_UNAVAILABLE(28, "Copy is not eligible for lending due to bad state or unavailability"),

  TRANSACTION_CREATION_FAILED(27, "Could not create new transaction."),
  BOOK_COPIES_NOT_IN_TRANSACTION(31, "Some book copies do not belong to customer's transactions"),
  INCORRECT_BOOK_COPY_STATUS(32, "Some book copies to return have incorrect status"),

  CUSTOMER_HAS_FEE(28, "Customer has pending fee."),
  CUSTOMER_HAS_THE_BOOK(29, "Customer has already this book."),

  INPUT_VALIDATIONS_ERROR(30, "Wrong input.");

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
