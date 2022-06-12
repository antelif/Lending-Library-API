package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static com.antelif.library.domain.type.BookCopyStatus.LENT;
import static com.antelif.library.domain.type.State.BAD;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequestWithFee;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionResponse;
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
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
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
      "Transaction: Unsuccessful creation when customer has lent this title and has active transaction.")
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
}
