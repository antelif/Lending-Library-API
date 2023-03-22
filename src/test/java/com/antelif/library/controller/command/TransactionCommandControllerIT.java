package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_COPIES_NOT_IN_TRANSACTION;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_FINALIZED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOKS_IN_TRANSACTION;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.TRANSACTION_DOES_NOT_EXIST;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;
import static com.antelif.library.domain.type.State.BAD;
import static com.antelif.library.domain.type.TransactionStatus.FINALIZED;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.config.BaseIT;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Transactions command controller")
@WithMockUser(username = "root", password = "root", roles = ADMIN)
class TransactionCommandControllerIT extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private TransactionResponse expectedTransactionResponse;
  private TransactionRequest transactionRequest;

  private String isbn;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    authorCounter++;
    publisherCounter++;
    bookCounter++;
    customerCounter++;
    personnelCounter++;

    expectedTransactionResponse =
        createTransactionResponse(
            customerCounter, personnelCounter, authorCounter, publisherCounter, bookCounter);

    AuthorRequest authorRequest = createAuthorRequest(authorCounter);
    AuthorResponse authorResponse = postAuthor(authorRequest, this.mockMvc);

    PublisherRequest publisherRequest = createPublisherRequest(publisherCounter);
    PublisherResponse publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    BookRequest bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    BookResponse bookResponse = postBook(bookRequest, this.mockMvc);
    isbn = bookResponse.getIsbn();

    BookCopyRequest bookCopyRequest = createBookCopyRequest(isbn);
    BookCopyResponse bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    CustomerRequest customerRequest = createCustomerRequest(customerCounter);
    CustomerResponse customerResponse = postCustomer(customerRequest, this.mockMvc);

    PersonnelRequest personnelRequest = createPersonnelRequest(personnelCounter);
    PersonnelResponse personnelResponse = postPersonnel(personnelRequest, this.mockMvc);

    transactionRequest =
        createTransactionRequest(
            customerResponse.getId(), personnelResponse.getId(), bookCopyResponse.getId());
  }

  @Test
  @DisplayName("Transaction: Successful creation.")
  @SneakyThrows
  void testTransactionIsCreatedSuccessfully() {

    TransactionResponse actualTransactionResponse = postTransaction(transactionRequest, this.mockMvc);

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

    BookCopyResponse expectedCopy = expectedTransactionResponse.getBooks().stream().findFirst().get();
    BookCopyResponse actualCopy = actualTransactionResponse.getBooks().stream().findFirst().get();

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
  void testTransactionFailsWhenCustomerBorrowsSameBookInOneTransaction() {

    // Create another book copy for the book of the transaction.
    BookCopyRequest secondBookCopyRequest = createBookCopyRequest(isbn);
    BookCopyResponse secondBookCopyResponse = postBookCopy(secondBookCopyRequest, this.mockMvc);

    // Add the second book copy to transaction request.
    List<Long> bookCopyIds =
        List.of(transactionRequest.getCopyIds().get(0), secondBookCopyResponse.getId());
    transactionRequest.setCopyIds(bookCopyIds);

    ErrorResponse transactionMapResponse =
        postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT, objectMapper.writeValueAsString(transactionRequest), mockMvc);
    assertEquals(DUPLICATE_BOOKS_IN_TRANSACTION.getCode(), transactionMapResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName(
      "Transaction: Unsuccessful creation when customer has borrowed this title and has active transaction.")
  void testTransactionFailsWhenCustomerHasLentThisBook() {
    // First transaction
    postTransaction(transactionRequest, mockMvc);

    ErrorResponse transactionResponse =
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

    BookCopyRequest bookCopyRequest = createBookCopyRequest(isbn);
    bookCopyRequest.setState(BAD);

    BookCopyResponse bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    transactionRequest.setCopyIds(List.of(bookCopyResponse.getId()));

    ErrorResponse transactionResponse =
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

    // A user lends the book.
    postTransaction(transactionRequest, this.mockMvc);

    // When another user tries to lend same book they fail.
    customerCounter++;
    CustomerRequest newCustomer = createCustomerRequest(customerCounter);
    Long newCustomerId = postCustomer(newCustomer, this.mockMvc).getId();

    transactionRequest.setCustomerId(newCustomerId);

    ErrorResponse transactionResponse =
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

    ErrorResponse transactionResponse =
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

    ErrorResponse transactionResponse =
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

    ErrorResponse transactionResponse =
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
    TransactionResponse transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    Long customerId = transactionResponse.getCustomer().getId();
    List<Long> bookCopyIds = transactionResponse.getBooks().stream().map(BookCopyResponse::getId).toList();

    List<TransactionResponse> updatedTransactions = patchTransactions(customerId, bookCopyIds, this.mockMvc);

    assertEquals(1, updatedTransactions.size());

    TransactionResponse updatedTransaction = updatedTransactions.get(0);
    assertEquals(FINALIZED, updatedTransaction.getStatus());

    assertEquals(1, updatedTransaction.getBooks().size());
    BookCopyResponse bookCopy = updatedTransaction.getBooks().stream().findAny().get();
    assertEquals(AVAILABLE, bookCopy.getStatus());
  }

  @Test
  @SneakyThrows
  @DisplayName(
      "Transaction: Unsuccessful update when book copies do not exist in active transaction")
  void testUnsuccessfulUpdateWhenBookCopyIdsDoNotExistInTransaction() {
    TransactionResponse transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    Long customerId = transactionResponse.getCustomer().getId();
    List<Long> bookCopyIds = List.of(9999L);

    ErrorResponse errorResponse =
        patchRequestAndExpectError(
            TRANSACTIONS_ENDPOINT + "/customer/" + customerId,
            objectMapper.writeValueAsString(bookCopyIds),
            this.mockMvc);

    assertEquals(BOOK_COPIES_NOT_IN_TRANSACTION.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Transaction: Successful cancellation.")
  void testSuccessfulCancellationWhenAllBookCopiesAreLent() {
    TransactionResponse transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    TransactionResponse result = cancelTransaction(transactionResponse.getId(), this.mockMvc);

    assertNotNull(result);
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when transaction does not exist.")
  void testUnsuccessfulTransactionWhenTransactionDoesNotExist() {

    long transactionId = 9999L;

    ErrorResponse response = cancelTransactionAndExpectError(transactionId, this.mockMvc);

    assertEquals(TRANSACTION_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when transaction is finalized.")
  void testUnsuccessfulTransactionCancellationWhenFinalized() {

    TransactionResponse transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    // Return books and finalize transaction.
    patchTransactions(
        transactionResponse.getCustomer().getId(),
        transactionResponse.getBooks().stream()
            .map(BookCopyResponse::getId)
            .collect(Collectors.toList()),
        this.mockMvc);

    ErrorResponse result = cancelTransactionAndExpectError(transactionResponse.getId(), this.mockMvc);

    assertEquals(CANNOT_CANCEL_FINALIZED_TRANSACTION.getCode(), result.getCode());
  }

  @Test
  @DisplayName("Transaction: Unsuccessful cancellation when some books are already returned.")
  void testUnsuccessfulTransactionCancellationWhenPartiallyReturned() {

    // Increase counters to create new book.
    bookCounter++;
    publisherCounter++;
    authorCounter++;

    // Create new book.
    AuthorRequest secondAuthorRequest = createAuthorRequest(authorCounter);
    AuthorResponse secondAuthorResponse = postAuthor(secondAuthorRequest, this.mockMvc);

    PublisherRequest secondPublisherRequest = createPublisherRequest(publisherCounter);
    PublisherResponse secondPublisherResponse = postPublisher(secondPublisherRequest, this.mockMvc);

    BookRequest secondBookRequest =
        createBookRequest(
            bookCounter, secondAuthorResponse.getId(), secondPublisherResponse.getId());

    BookResponse secondBookResponse = postBook(secondBookRequest, this.mockMvc);
    isbn = secondBookResponse.getIsbn();

    // Create book copy for the new book.
    BookCopyRequest secondBookCopyRequest = createBookCopyRequest(isbn);
    BookCopyResponse secondBookCopyResponse = postBookCopy(secondBookCopyRequest, this.mockMvc);

    List<Long> copyIds = List.of(transactionRequest.getCopyIds().get(0), secondBookCopyResponse.getId());

    // Add the new book copy to new transaction.
    transactionRequest.setCopyIds(new ArrayList<>(copyIds));
    transactionRequest.getCopyIds().add(secondBookCopyResponse.getId());

    TransactionResponse transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    // Return the new book copy.
    patchTransactions(
        transactionRequest.getCustomerId(), List.of(secondBookCopyResponse.getId()), this.mockMvc);

    // Try to cancel transaction when the new book is already returned.
    ErrorResponse result = cancelTransactionAndExpectError(transactionResponse.getId(), this.mockMvc);

    assertEquals(CANNOT_CANCEL_PARTIALLY_UPDATED_TRANSACTION.getCode(), result.getCode());
  }
}
