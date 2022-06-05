package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_UNAVAILABLE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_FEE;
import static com.antelif.library.application.error.GenericError.CUSTOMER_HAS_THE_BOOK;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
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
import static com.antelif.library.utils.Request.postAuthor;
import static com.antelif.library.utils.Request.postBook;
import static com.antelif.library.utils.Request.postBookCopy;
import static com.antelif.library.utils.Request.postCustomer;
import static com.antelif.library.utils.Request.postPersonnel;
import static com.antelif.library.utils.Request.postPublisher;
import static com.antelif.library.utils.Request.postTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
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
    var publisherRequest = createPublisherRequest(publisherCounter);

    // Set author id
    var authorMap =
        objectMapper.readValue(
            postAuthor(objectMapper.writeValueAsString(authorRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var authorId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class)
            .getId();

    // Set publisher id
    var publisherMap =
        objectMapper.readValue(
            postPublisher(objectMapper.writeValueAsString(publisherRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var publisherId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class)
            .getId();

    var bookRequest = createBookRequest(bookCounter, authorId, publisherId);

    // Set book isbn
    var bookMap =
        objectMapper.readValue(
            postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    isbn =
        objectMapper
            .readValue(objectMapper.writeValueAsString(bookMap.get(CREATED)), BookResponse.class)
            .getIsbn();

    var bookCopyMap =
        objectMapper.readValue(
            postBookCopy(
                objectMapper.writeValueAsString(createBookCopyRequest(isbn)), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var bookCopyId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(bookCopyMap.get(CREATED)), BookCopyResponse.class)
            .getId();

    var customerMap =
        objectMapper.readValue(
            postCustomer(
                objectMapper.writeValueAsString(createCustomerRequest(customerCounter)),
                this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var customerId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(customerMap.get(CREATED)), CustomerResponse.class)
            .getId();

    var personnelMap =
        objectMapper.readValue(
            postPersonnel(
                objectMapper.writeValueAsString(createPersonnelRequest(personnelCounter)),
                this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var personnelId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(personnelMap.get(CREATED)), PersonnelResponse.class)
            .getId();

    transactionRequest = createTransactionRequest(customerId, personnelId, bookCopyId);
  }

  @Test
  @DisplayName("Transaction: Successful creation.")
  @SneakyThrows
  void testTransactionIsCreatedSuccessfully() {

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<Map<String, Object>>() {});

    var actualTransactionResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(transactionMapResponse.get(CREATED)),
            TransactionResponse.class);

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

    var customerMap =
        objectMapper.readValue(
            postCustomer(objectMapper.writeValueAsString(customerRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var customerId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(customerMap.get(CREATED)), CustomerResponse.class)
            .getId();

    transactionRequest.setCustomerId(customerId);

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});
    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(CUSTOMER_HAS_FEE.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName(
      "Transaction: Unsuccessful creation when customer has lent this title and has active transaction.")
  void testTransactionFailsWhenCustomerHasLentThisBook() {
    // First transaction
    postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc);

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});
    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(CUSTOMER_HAS_THE_BOOK.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy state is bad.")
  void testTransactionFailsWhenBookStateIsBad() {

    var bookCopyRequest = createBookCopyRequest(isbn);
    bookCopyRequest.setState(BAD);

    var bookCopyMap =
        objectMapper.readValue(
            postBookCopy(objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var bookCopyId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(bookCopyMap.get(CREATED)), BookCopyResponse.class)
            .getId();

    transactionRequest.setCopyIds(List.of(bookCopyId));

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});

    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(BOOK_COPY_UNAVAILABLE.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy status is lent.")
  void testTransactionFailsWhenBookStatusIsLent() {

    var bookCopyRequest = createBookCopyRequest(isbn);
    bookCopyRequest.setStatus(LENT);

    var bookCopyMap =
        objectMapper.readValue(
            postBookCopy(objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var bookCopyId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(bookCopyMap.get(CREATED)), BookCopyResponse.class)
            .getId();

    transactionRequest.setCopyIds(List.of(bookCopyId));

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});

    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(BOOK_COPY_UNAVAILABLE.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when copy ids provided retrieve no books.")
  void testTransactionFailsWhenBookCopiesAreEmpty() {

    transactionRequest.setCopyIds(List.of(9999L));

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});

    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(BOOK_COPY_DOES_NOT_EXIST.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when customer does not exist.")
  void testTransactionFailsWhenCustomerDoesNotExist() {
    transactionRequest.setCustomerId((9999L));

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});

    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(CUSTOMER_DOES_NOT_EXIST.getCode(), transactionMapResponse.get(0).getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Transaction: Unsuccessful creation when personnel does not exist.")
  void testTransactionFailsWhenPersonnelDoesNotExist() {
    transactionRequest.setPersonnelId(9999L);

    var transactionMapResponse =
        objectMapper.readValue(
            postTransaction(objectMapper.writeValueAsString(transactionRequest), mockMvc),
            new TypeReference<List<ErrorResponse>>() {});

    assertTrue(transactionMapResponse.size() > 0);
    assertEquals(PERSONNEL_DOES_NOT_EXIST.getCode(), transactionMapResponse.get(0).getCode());
  }
}
