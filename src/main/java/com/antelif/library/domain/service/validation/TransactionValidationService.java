package com.antelif.library.domain.service.validation;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;

import com.antelif.library.domain.exception.UnsuccessfulTransactionException;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.entity.TransactionItemEntity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Transaction validation service. */
@Component
@RequiredArgsConstructor
public class TransactionValidationService {

  /**
   * Contains all validation for new transactions.
   *
   * @param customer the customer of the transaction,
   * @param bookCopies the book copies that will be lent in this transaction.
   */
  public void validate(CustomerEntity customer, List<BookCopyEntity> bookCopies) {

    validateBookCopiesRetrieved(bookCopies);
    validateCustomerHasPendingFee(customer);
    validateCustomerHasThisBook(customer, bookCopies);
    validateCopyIsNotEligibleForLending(bookCopies);
  }

  private void validateBookCopiesRetrieved(List<BookCopyEntity> bookCopies) {
    if (bookCopies.isEmpty()) {
      throw new UnsuccessfulTransactionException(BOOK_COPY_DOES_NOT_EXIST);
    }
  }

  private void validateCustomerHasPendingFee(CustomerEntity customer) {

    if (customer.getFee() > 0) {
      throw new UnsuccessfulTransactionException(CUSTOMER_HAS_FEE);
    }
  }

  private void validateCustomerHasThisBook(
      CustomerEntity customer, List<BookCopyEntity> bookCopies) {
    var bookCopyIds =
        Stream.of(bookCopies)
            .flatMap(Collection::stream)
            .map(BookCopyEntity::getId)
            .collect(Collectors.toSet());
    var customerBookCopyIds =
        Stream.of(customer)
            .map(CustomerEntity::getTransactions)
            .flatMap(Collection::stream)
            .filter(t -> t.getStatus().equals(ACTIVE))
            .map(TransactionEntity::getTransactionItems)
            .flatMap(Collection::stream)
            .map(TransactionItemEntity::getBookCopy)
            .map(BookCopyEntity::getId)
            .collect(Collectors.toSet());
    if (bookCopyIds.stream().anyMatch(customerBookCopyIds::contains)) {
      throw new UnsuccessfulTransactionException(CUSTOMER_HAS_THE_BOOK);
    }
  }

  private void validateCopyIsNotEligibleForLending(List<BookCopyEntity> bookCopies) {
    if (bookCopies.stream().anyMatch(bookCopy -> !bookCopy.isEligibleToLent())) {
      throw new UnsuccessfulTransactionException(BOOK_COPY_UNAVAILABLE);
    }
  }
}
