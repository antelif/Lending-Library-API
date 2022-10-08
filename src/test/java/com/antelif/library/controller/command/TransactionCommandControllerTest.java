package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_COPIES_NOT_IN_TRANSACTION;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_FINALIZED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.TRANSACTION_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;
import static com.antelif.library.domain.type.BookCopyStatus.LENT;
import static com.antelif.library.domain.type.State.BAD;
import static com.antelif.library.domain.type.TransactionStatus.FINALIZED;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequestWithFee;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionResponse;
import static com.antelif.library.utils.RequestBuilder.cancelTransaction;
import static com.antelif.library.utils.RequestBuilder.cancelTransactionAndExpectError;
import static com.antelif.library.utils.RequestBuilder.patchRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.patchTransactions;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static com.antelif.library.utils.RequestBuilder.postBookCopy;
import static com.antelif.library.utils.RequestBuilder.postCustomer;
import static com.antelif.library.utils.RequestBuilder.postPersonnel;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Transactions command controller")
class TransactionCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private TransactionResponse expectedTransactionResponse;
  private TransactionRequest transactionRequest;

  private String isbn;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;
    publisherCounter++;
    bookCounter++;
    customerCounter++;
    personnelCounter++;

    expectedTransactionResponse =
        createTransactionResponse(
            customerCounter, personnelCounter, authorCounter, publisherCounter, bookCounter);

    var authorRequest = createAuthorRequest(authorCounter);
    var authorResponse = postAuthor(authorRequest, this.mockMvc);

    var publisherRequest = createPublisherRequest(publisherCounter);
    var publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    var bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    var bookResponse = postBook(bookRequest, this.mockMvc);
    isbn = bookResponse.getIsbn();

    var bookCopyRequest = createBookCopyRequest(isbn);
    var bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    var customerRequest = createCustomerRequest(customerCounter);
    var customerResponse = postCustomer(customerRequest, this.mockMvc);

    var personnelRequest = createPersonnelRequest(personnelCounter);
    var personnelResponse = postPersonnel(personnelRequest, this.mockMvc);

    transactionRequest =
        createTransactionRequest(
            customerResponse.getId(), personnelResponse.getId(), bookCopyResponse.getId());
  }

  @Test
  @DisplayName("Transaction: Successful creation.")
  @SneakyThrows
  void testTransactionIsCreatedSuccessfully() {

    var actualTransactionResponse = postTransaction(transactionRequest, this.mockMvc);

    assertNotNull(actualTransactionResponse);

    assertNotNull(actualTransactionResponse.getId());
    assertNotNull(actualTransactionResponse.getCreationDate());
    assertNotNull(actualTransactionResponse.getReturnDate());
    assertEquals(expectedTransactionResponse.getStatus(), actualTransactionResponse.getStatus());

    assertNotNull(actualTransactionResponse.getCustomer().getId());
    assertEquals(
        expectedTransactionResponse.getCustomer().getName(),
        actualTransactionResponse.getCustomer().getName());
    assertEquals(
        expectedTransactionResponse.getCustomer().getSurname(),
        actualTransactionResponse.getCustomer().getSurname());
    assertEquals(
        expectedTransactionResponse.getCustomer().getPhoneNo(),
        actualTransactionResponse.getCustomer().getPhoneNo());
    assertEquals(
        expectedTransactionResponse.getCustomer().getEmail(),
        actualTransactionResponse.getCustomer().getEmail());
    assertEquals(
        expectedTransactionResponse.getCustomer().getFee(),
        actualTransactionResponse.getCustomer().getFee());

    assertNotNull(actualTransactionResponse.getPersonnel().getId());
    assertEquals(
        expectedTransactionResponse.getPersonnel().getUsername(),
        actualTransactionResponse.getPersonnel().getUsername());

    var expectedCopy = expectedTransactionResponse.getBooks().stream().findFirst().get();
    var actualCopy = actualTransactionResponse.getBooks().stream().findFirst().get();

    assertNotNull(actualCopy.getId());
    assertEquals(expectedCopy.getState(), actualCopy.getState());
    assertEquals(expectedCopy.getStatus(), actualCopy.getStatus());

    assertNotNull(actualCopy.getBook().getId());
    assertEquals(expectedCopy.getBook().getTitle(), actualCopy.getBook().getTitle());
    assertEquals(expectedCopy.getBook().getIsbn(), actualCopy.getBook().getIsbn());

    assertNotNull(actualCopy.getBook().getPublisher().getId());
    assertEquals(
        expectedCopy.getBook().getPublisher().getName(),
        actualCopy.getBook().getPublisher().getName());

    assertNotNull(actualCopy.getBook().getAuthor().getId());
    assertEquals(
        expectedCopy.getBook().getAuthor().getName(), actualCopy.getBook().getAuthor().getName());
    assertEquals(
        expectedCopy.getBook().getAuthor().getMiddleName(),
        actualCopy.getBook().getAuthor().getMiddleName());
    assertEquals(
        expectedCopy.getBook().getAuthor().getSurname(),
        actualCopy.getBook().getAuthor().getSurname());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when customer has fee pending.")
  void testTransactionFailsWhenCustomerHasFee() {

    customerCounter++;
    var customerRequest = createCustomerRequestWithFee(customerCounter);
    var customerResponse = postCustomer(customerRequest, this.mockMvc);

    transactionRequest.setCustomerId(customerResponse.getId());

    var transactionMapResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT, objectMapper.writeValueAsString(transactionRequest), mockMvc);
    assertEquals(CUSTOMER_HAS_FEE.getCode(), transactionMapResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName(
      "Transaction: Unsuccessful creation when customer has borrowed this title and has active transaction.")
  void testTransactionFailsWhenCustomerHasLentThisBook() {
    // First transaction
    postTransaction(transactionRequest, mockMvc);

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);
    assertEquals(CUSTOMER_HAS_THE_BOOK.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy state is bad.")
  void testTransactionFailsWhenBookStateIsBad() {

    var bookCopyRequest = createBookCopyRequest(isbn);
    bookCopyRequest.setState(BAD);

    var bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    transactionRequest.setCopyIds(List.of(bookCopyResponse.getId()));

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(BOOK_COPY_UNAVAILABLE.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy status is lent.")
  void testTransactionFailsWhenBookStatusIsLent() {

    var bookCopyRequest = createBookCopyRequest(isbn);
    bookCopyRequest.setStatus(LENT);

    var bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    transactionRequest.setCopyIds(List.of(bookCopyResponse.getId()));

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(BOOK_COPY_UNAVAILABLE.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy ids provided retrieve no books.")
  void testTransactionFailsWhenBookCopiesAreEmpty() {

    transactionRequest.setCopyIds(List.of(9999L));

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(BOOK_COPY_DOES_NOT_EXIST.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when customer does not exist.")
  void testTransactionFailsWhenCustomerDoesNotExist() {
    transactionRequest.setCustomerId((9999L));

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(CUSTOMER_DOES_NOT_EXIST.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when personnel does not exist.")
  void testTransactionFailsWhenPersonnelDoesNotExist() {
    transactionRequest.setPersonnelId(9999L);

    var transactionResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(PERSONNEL_DOES_NOT_EXIST.getCode(), transactionResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Successful update of transaction")
  void testSuccessfulUpdateOfTransaction() {
    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    var customerId = transactionResponse.getCustomer().getId();
    var bookCopyIds = transactionResponse.getBooks().stream().map(BookCopyResponse::getId).toList();

    var updatedTransactions = patchTransactions(customerId, bookCopyIds, this.mockMvc);

    assertEquals(1, updatedTransactions.size());

    var updatedTransaction = updatedTransactions.get(0);
    assertEquals(FINALIZED, updatedTransaction.getStatus());

    assertEquals(1, updatedTransaction.getBooks().size());
    var bookCopy = updatedTransaction.getBooks().stream().findAny().get();
    assertEquals(AVAILABLE, bookCopy.getStatus());
  }

  @Test
  @SneakyThrows
  @DisplayName(
      "Transaction: Unsuccessful update when book copies do not exist in active transaction")
  void testUnsuccessfulUpdateWhenBookCopyIdsDoNotExistInTransaction() {
    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    var customerId = transactionResponse.getCustomer().getId();
    var bookCopyIds = List.of(9999L);

    var errorResponse =
        patchRequestAndExpectError(
            TRANSACTIONS_ENDPOINT + "/customer/" + customerId,
            objectMapper.writeValueAsString(bookCopyIds),
            this.mockMvc);

    assertEquals(BOOK_COPIES_NOT_IN_TRANSACTION.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Transaction: Successful cancellation.")
  void testSuccessfulCancellationWhenAllBookCopiesAreLent() {
    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    var result = cancelTransaction(transactionResponse.getId(), this.mockMvc);

    assertNotNull(result);
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when transaction does not exist.")
  void testUnsuccessfulTransactionWhenTransactionDoesNotExist() {

    var transactionId = 9999L;

    var response = cancelTransactionAndExpectError(transactionId, this.mockMvc);

    assertEquals(TRANSACTION_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when transaction is finalized.")
  void testUnsuccessfulTransactionCancellationWhenFinalized() {

    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    // Return books and finalize transaction.
    patchTransactions(
        transactionResponse.getCustomer().getId(),
        transactionResponse.getBooks().stream()
            .map(BookCopyResponse::getId)
            .collect(Collectors.toList()),
        this.mockMvc);

    var result = cancelTransactionAndExpectError(transactionResponse.getId(), this.mockMvc);

    assertEquals(CANNOT_CANCEL_FINALIZED_TRANSACTION.getCode(), result.getCode());
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when some books are already returned.")
  void testUnsuccessfulTransactionCancellationWhenPartiallyReturned() {

    // Create additional book copy.
    var secondBookCopyRequest = createBookCopyRequest(isbn);
    var secondBookCopyResponse = postBookCopy(secondBookCopyRequest, this.mockMvc);

    var copyIds = List.of(transactionRequest.getCopyIds().get(0), secondBookCopyResponse.getId());

    // Add the book copy to new transaction.
    transactionRequest.setCopyIds(new ArrayList<>(copyIds));
    transactionRequest.getCopyIds().add(secondBookCopyResponse.getId());

    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    // Return second book copy.
    patchTransactions(
        transactionRequest.getCustomerId(), List.of(secondBookCopyResponse.getId()), this.mockMvc);

    // try to cancel transaction when one book is already returned.
    var result = cancelTransactionAndExpectError(transactionResponse.getId(), this.mockMvc);

    assertEquals(CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION.getCode(), result.getCode());
  }
}
