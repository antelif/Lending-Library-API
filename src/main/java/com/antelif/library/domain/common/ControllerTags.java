package com.antelif.library.domain.common;

/** Controller tags used to separate requests in Swagger. */
public final class ControllerTags {

  private ControllerTags() {}

  // Separate controller by logic.
  public static final String BOOK_CONTROLLER = "Book";
  public static final String BOOK_COPY_CONTROLLER = "Book Copy";
  public static final String AUTHOR_CONTROLLER = "Author";
  public static final String PUBLISHER_CONTROLLER = "Publisher";
  public static final String TRANSACTION_CONTROLLER = "Transaction";
  public static final String PERSONNEL_CONTROLLER = "Personnel";
  public static final String CUSTOMER_CONTROLLER = "Customer";

  // Separate controller by function.
  public static final String POST_CONTROLLER = "POST";
  public static final String PUT_CONTROLLER = "PUT";
  public static final String GET_CONTROLLER = "GET";
  public static final String DELETE_CONTROLLER = "DELETE";
}
