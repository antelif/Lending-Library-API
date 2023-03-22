package com.antelif.library.domain.service.validation;

import static com.antelif.library.application.error.GenericError.BOOK_COPIES_NOT_IN_TRANSACTION;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_FINALIZED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOKS_IN_TRANSACTION;
import static com.antelif.library.application.error.GenericError.INCORRECT_BOOK_COPY_STATUS;
import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;
import static com.antelif.library.domain.type.BookCopyStatus.LENT;
import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;
import static com.antelif.library.domain.type.TransactionStatus.FINALIZED;

import com.antelif.library.domain.exception.UnsuccessfulTransactionException;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.entity.TransactionItemEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Transaction validation service.
 */
@Component
public final class TransactionValidationService {

  /**
   * Contains all validation for new transactions.
   *
   * @param transaction the transaction to validate.
   */
  public static void validateCreation(TransactionEntity transaction) {

    List<BookCopyEntity> bookCopies =
        Optional.of(transaction).map(TransactionEntity::getTransactionItems).stream()
            .flatMap(Collection::stream)
            .map(TransactionItemEntity::getBookCopy)
            .collect(Collectors.toList());
    CustomerEntity customer = transaction.getCustomer();

    validateBookCopiesRetrieved(bookCopies);
    validateBooksToBorrowAreUnique(bookCopies);
    validateCustomerHasPendingFee(customer);
    validateCustomerHasThisBook(customer, bookCopies);
    validateCopyIsNotEligibleForLending(bookCopies);
  }

  /**
   * Contains all validations for transactions to be updated.
   *
   * @param transactions        a list of TransactionEntity items with information about
   *                            transactions to be updated,
   * @param bookCopyIdsReturned a list of book copy ids to be returned.
   */
  public static void validateUpdate(
      List<TransactionEntity> transactions, List<Long> bookCopyIdsReturned) {

    validateBookCopiesToReturnExistInTransaction(transactions, bookCopyIdsReturned);
    validateBookCopiesToReturnAreLent(transactions, bookCopyIdsReturned);
  }

  /**
   * Throws an exception if the transaction provided to cancel has been finalized.
   *
   * @param transaction the transaction to cancel.
   */
  public static void validateCancel(TransactionEntity transaction) {
    validateTransactionIsFinalized(transaction);
    validateTransactionIsPartiallyUpdated(transaction);
  }

  /**
   * Throws an exception if the transaction to cancel is finalized.
   */
  private static void validateTransactionIsFinalized(TransactionEntity transaction) {
    if (transaction.getStatus().equals(FINALIZED)) {
      throw new UnsuccessfulTransactionException(CANNOT_CANCEL_FINALIZED_TRANSACTION);
    }
  }

  /**
   * Throws an exception if the transaction to cancel has some books returned already.
   */
  private static void validateTransactionIsPartiallyUpdated(TransactionEntity transaction) {
    if (transaction.getTransactionItems().stream()
        .map(TransactionItemEntity::getBookCopy)
        .map(BookCopyEntity::getStatus)
        .anyMatch(status -> status.equals(AVAILABLE))) {
      throw new UnsuccessfulTransactionException(CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION);
    }
  }

  /**
   * Throws an exception if there are books copies to return that do not exist in customer's active
   * transactions.
   */
  private static void validateBookCopiesToReturnExistInTransaction(
      List<TransactionEntity> transactions, List<Long> bookCopyIdsReturned) {
    List<Long> bookCopyIdsInTransactions =
        transactions.stream()
            .map(TransactionEntity::getTransactionItems)
            .flatMap(Collection::stream)
            .map(TransactionItemEntity::getBookCopy)
            .map(BookCopyEntity::getId)
            .toList();

    if (bookCopyIdsReturned.stream().anyMatch(id -> !bookCopyIdsInTransactions.contains(id))) {
      throw new UnsuccessfulTransactionException(BOOK_COPIES_NOT_IN_TRANSACTION);
    }
  }

  /**
   * Throws an exception if book copy id to return is not of status 'LENT' in transaction.
   */
  private static void validateBookCopiesToReturnAreLent(
      List<TransactionEntity> transactions, List<Long> bookCopyIdsReturned) {

    Set<BookCopyEntity> availableBookCopies =
        transactions.stream()
            .map(TransactionEntity::getTransactionItems)
            .flatMap(Collection::stream)
            .map(TransactionItemEntity::getBookCopy)
            .filter(
                bookCopy ->
                    bookCopyIdsReturned.contains(bookCopy.getId())
                        && !bookCopy.getStatus().equals(LENT))
            .collect(Collectors.toSet());
    if (0 < availableBookCopies.size()) {
      throw new UnsuccessfulTransactionException(INCORRECT_BOOK_COPY_STATUS);
    }
  }

  private static void validateBookCopiesRetrieved(List<BookCopyEntity> bookCopies) {
    if (bookCopies.isEmpty()) {
      throw new UnsuccessfulTransactionException(BOOK_COPY_DOES_NOT_EXIST);
    }
  }

  /**
   * Throws an exception if transaction contains books with the same ISBN twice.
   */
  private static void validateBooksToBorrowAreUnique(List<BookCopyEntity> bookCopies) {
    if (bookCopies.stream()
        .map(BookCopyEntity::getBook)
        .map(BookEntity::getIsbn)
        .collect(Collectors.toSet())
        .size()
        < bookCopies.size()) {
      throw new UnsuccessfulTransactionException(DUPLICATE_BOOKS_IN_TRANSACTION);
    }
  }

  private static void validateCustomerHasPendingFee(CustomerEntity customer) {

    if (customer.getFee() > 0) {
      throw new UnsuccessfulTransactionException(CUSTOMER_HAS_FEE);
    }
  }

  private static void validateCustomerHasThisBook(
      CustomerEntity customer, List<BookCopyEntity> bookCopies) {
    Set<Long> bookCopyIds =
        Stream.of(bookCopies)
            .flatMap(Collection::stream)
            .map(BookCopyEntity::getId)
            .collect(Collectors.toSet());
    Set<Long> customerBookCopyIds =
        Stream.of(customer)
            .map(CustomerEntity::getTransactions)
            .flatMap(Collection::stream)
            .filter(transaction -> transaction.getStatus().equals(ACTIVE))
            .map(TransactionEntity::getTransactionItems)
            .flatMap(Collection::stream)
            .map(TransactionItemEntity::getBookCopy)
            .filter(bookCopy -> bookCopy.getStatus().equals(LENT))
            .map(BookCopyEntity::getId)
            .collect(Collectors.toSet());
    if (bookCopyIds.stream().anyMatch(customerBookCopyIds::contains)) {
      throw new UnsuccessfulTransactionException(CUSTOMER_HAS_THE_BOOK);
    }
  }

  private static void validateCopyIsNotEligibleForLending(List<BookCopyEntity> bookCopies) {
    if (bookCopies.stream().anyMatch(bookCopy -> !bookCopy.isEligibleToLent())) {
      throw new UnsuccessfulTransactionException(BOOK_COPY_UNAVAILABLE);
    }
  }

  private TransactionValidationService() {
  }
}
