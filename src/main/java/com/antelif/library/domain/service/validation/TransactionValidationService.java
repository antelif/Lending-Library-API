package com.antelif.library.domain.service.validation;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;

import com.antelif.library.application.error.GenericError;
import com.antelif.library.domain.exception.UnsuccessfulTransaction;
import com.antelif.library.domain.type.TransactionStatus;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.entity.TransactionItemEntity;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    var errors =
        Stream.of(
                validateBookCopiesRetrieved(bookCopies),
                validateCustomerHasPendingFee(customer),
                validateCustomerHasThisBook(customer, bookCopies),
                validateCopyIsNotEligibleForLending(bookCopies))
            .filter(Objects::nonNull)
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

    if (!errors.isEmpty()) {
      throw new UnsuccessfulTransaction(errors);
    }
  }

  private Optional<GenericError> validateBookCopiesRetrieved(List<BookCopyEntity> bookCopies) {
    // TODO: Throw assigned exception or somehow include arguments of the error.
    return bookCopies.isEmpty() ? Optional.of(BOOK_COPY_DOES_NOT_EXIST) : Optional.empty();
  }

  private Optional<GenericError> validateCustomerHasPendingFee(CustomerEntity customer) {

    return customer.getFee() > 0 ? Optional.of(CUSTOMER_HAS_FEE) : Optional.empty();
  }

  private Optional<GenericError> validateCustomerHasThisBook(
      CustomerEntity customer, List<BookCopyEntity> bookCopies) {
    return bookCopies.stream()
            .map(
                copy ->
                    Optional.ofNullable(customer.getTransactions()).stream()
                        .flatMap(Collection::stream)
                        .filter(t -> t.getStatus().equals(TransactionStatus.ACTIVE))
                        .map(TransactionEntity::getTransactionItems)
                        .flatMap(Collection::stream)
                        .map(TransactionItemEntity::getBookCopy)
                        .map(BookCopyEntity::getBook)
                        .map(BookEntity::getIsbn)
                        .anyMatch(isbn -> isbn.equals(copy.getBook().getIsbn())))
            .collect(Collectors.toSet())
            .stream()
            .anyMatch(b -> b)
        ? Optional.of(CUSTOMER_HAS_THE_BOOK)
        : Optional.empty();
  }

  private Optional<GenericError> validateCopyIsNotEligibleForLending(
      List<BookCopyEntity> bookCopies) {
    return bookCopies.stream().anyMatch(bookCopy -> !bookCopy.isEligibleToLent())
        ? Optional.of(BOOK_COPY_UNAVAILABLE)
        : Optional.empty();
  }
}
