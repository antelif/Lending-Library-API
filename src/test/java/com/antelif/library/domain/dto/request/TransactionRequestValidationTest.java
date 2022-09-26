package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionRequest;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static com.antelif.library.utils.RequestBuilder.postBookCopy;
import static com.antelif.library.utils.RequestBuilder.postCustomer;
import static com.antelif.library.utils.RequestBuilder.postPersonnel;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.integration.BaseIntegrationTest;
import com.antelif.library.utils.RequestBuilder;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Validations Transaction")
public class TransactionRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private TransactionRequest transactionRequest;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    customerCounter++;
    personnelCounter++;
    bookCounter++;
    authorCounter++;
    publisherCounter++;

    var authorRequest = createAuthorRequest(authorCounter);
    var authorResponse = postAuthor(authorRequest, this.mockMvc);

    var publisherRequest = createPublisherRequest(publisherCounter);
    var publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    var bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    var bookResponse = postBook(bookRequest, this.mockMvc);
    var isbn = bookResponse.getIsbn();

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
  @DisplayName("Transaction Validations: days until return cannot be more than 7.")
  @SneakyThrows
  void testPublisherDaysUntilReturnCannotBeMoreThan7() {
    transactionRequest.setDaysUntilReturn(8);

    var response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction Validations: days until return cannot be less than 1.")
  @SneakyThrows
  void testPublisherDaysUntilReturnCannotBeLessThan1() {
    transactionRequest.setDaysUntilReturn(0);

    var response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction Validations: customer cannot be null.")
  @SneakyThrows
  void testPublisherCustomerCannotBeNull() {
    transactionRequest.setCustomerId(null);

    var response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction Validations: personnel cannot be null.")
  @SneakyThrows
  void testPublisherPersonnelCannotBeNull() {
    transactionRequest.setPersonnelId(null);

    var response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Transaction Validations: book copies list cannot be empty.")
  @SneakyThrows
  void testPublisherCBookCopiesListCannotBeEmpty() {
    transactionRequest.setCopyIds(new ArrayList<>());

    var response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
