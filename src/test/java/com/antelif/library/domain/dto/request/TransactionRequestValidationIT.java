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

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.config.BaseIT;
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
public class TransactionRequestValidationIT extends BaseIT {

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

    AuthorRequest authorRequest = createAuthorRequest(authorCounter);
    AuthorResponse authorResponse = postAuthor(authorRequest, this.mockMvc);

    PublisherRequest publisherRequest = createPublisherRequest(publisherCounter);
    PublisherResponse publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    BookRequest bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    BookResponse bookResponse = postBook(bookRequest, this.mockMvc);
    String isbn = bookResponse.getIsbn();

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
  @DisplayName("Transaction Validations: days until return cannot be more than 7.")
  @SneakyThrows
  void testPublisherDaysUntilReturnCannotBeMoreThan7() {
    transactionRequest.setDaysUntilReturn(8);

    ErrorResponse response =
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

    ErrorResponse response =
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

    ErrorResponse response =
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

    ErrorResponse response =
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

    ErrorResponse response =
        RequestBuilder.postRequestAndExpectError(
            TRANSACTIONS_ENDPOINT,
            objectMapper.writeValueAsString(transactionRequest),
            this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
