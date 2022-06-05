package com.antelif.library.factory;

import static com.antelif.library.factory.BookCopyFactory.createBookCopyResponseAfterTransaction;
import static com.antelif.library.factory.CustomerFactory.createCustomerResponse;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelResponse;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.type.TransactionStatus;
import java.util.List;
import java.util.Set;

public class TransactionFactory {

  public static TransactionRequest createTransactionRequest(
      long customerIndex, long personnelIndex, long bookCopyIndex) {
    var transaction = new TransactionRequest();
    transaction.setDaysUntilReturn(5);
    transaction.setCustomerId(customerIndex);
    transaction.setPersonnelId(personnelIndex);
    transaction.setCopyIds(List.of(bookCopyIndex));

    return transaction;
  }

  public static TransactionResponse createTransactionResponse(
      int customerIndex, int personnelIndex, int authorIndex, int publisherIndex, int bookIndex) {
    var transaction = new TransactionResponse();

    transaction.setCustomer(createCustomerResponse(customerIndex));
    transaction.setPersonnel(createPersonnelResponse(personnelIndex));
    transaction.setStatus(TransactionStatus.ACTIVE);
    transaction.setBooks(
        Set.of(createBookCopyResponseAfterTransaction(authorIndex, publisherIndex, bookIndex)));

    return transaction;
  }
}
