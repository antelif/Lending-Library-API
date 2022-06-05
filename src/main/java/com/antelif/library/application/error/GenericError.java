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
  BOOK_DOES_NOT_EXIST(10, "Book does not exist."),
  BOOK_CONVERTER_FAILED(11, "Could not convert book object."),
  BOOK_CREATION_FAILED(12, "Could not create new book."),

  DUPLICATE_PERSONNEL(13, "Personnel already exists."),
  PERSONNEL_DOES_NOT_EXIST(14, "Personnel does not exist."),
  PERSONNEL_CONVERTER_FAILED(15, "Could not convert personnel object."),
  PERSONNEL_CREATION_FAILED(16, "Could not create new personnel."),

  DUPLICATE_CUSTOMER(17, "Customer already exists."),
  CUSTOMER_DOES_NOT_EXIST(18, "Customer does not exist."),
  CUSTOMER_CONVERTER_FAILED(19, "Could not convert customer object."),
  CUSTOMER_CREATION_FAILED(20, "Could not create new customer."),

  DUPLICATE_BOOK_COPY(21, "Book copy already exists"),
  BOOK_COPY_DOES_NOT_EXIST(22, "Book copy does not exist."),
  BOOK_COPY_CONVERTER_FAILED(23, "Could not convert book copy object."),
  BOOK_COPY_CREATION_FAILED(24, "Could not create new book copy."),
  BOOK_COPY_UNAVAILABLE(28, "Copy is not eligible for lending due to bad state or unavailability"),

  TRANSACTION_DOES_NOT_EXIST(25, "Transaction does not exist."),
  TRANSACTION_CONVERTER_FAILED(26, "Could not convert transaction object."),
  TRANSACTION_CREATION_FAILED(27, "Could not create new transaction."),

  CUSTOMER_HAS_FEE(28, "Customer has pending fee."),
  CUSTOMER_HAS_THE_BOOK(29, "Customer has already this book.");

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
