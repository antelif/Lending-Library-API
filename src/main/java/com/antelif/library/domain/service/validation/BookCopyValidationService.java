package com.antelif.library.domain.service.validation;

import static com.antelif.library.application.error.GenericError.INVALID_BOOK_COPY_STATE;
import static com.antelif.library.application.error.GenericError.INVALID_BOOK_COPY_STATUS;
import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;

import com.antelif.library.domain.exception.ValidationException;
import com.antelif.library.domain.type.State;
import com.antelif.library.infrastructure.entity.BookCopyEntity;

public final class BookCopyValidationService {

  private BookCopyValidationService() {
  }

  /**
   * Contains all validations when updating a book copy.
   *
   * @param bookCopy the book copy to update,
   * @param state    the new book copy state
   */
  public static void validateUpdate(BookCopyEntity bookCopy, State state) {
    validateStateIsEligible(bookCopy, state);
    validateStatusIsEligible(bookCopy);
  }

  /**
   * Throws an exception if the new state is of better condition than the existing one.
   */
  private static void validateStateIsEligible(BookCopyEntity bookCopy, State state) {
    if (bookCopy.getState().getStateOrder() > state.getStateOrder()) {
      throw new ValidationException(
          INVALID_BOOK_COPY_STATE,
          "Existing state: " + bookCopy.getState() + ". New state: " + state);
    }
  }

  /**
   * Throws an exception if the book copy is not available.
   */
  private static void validateStatusIsEligible(BookCopyEntity bookCopy) {
    if (!bookCopy.getStatus().equals(AVAILABLE)) {
      throw new ValidationException(INVALID_BOOK_COPY_STATUS);
    }
  }
}
