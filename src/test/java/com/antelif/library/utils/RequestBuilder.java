package com.antelif.library.utils;

import static com.antelif.library.domain.common.Constants.CANCELLED;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Constants.UPDATED;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOK_COPIES_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.CANCEL_TRANSACTION_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.CharEncoding.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.request.update.BookCopyUpdateStateRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.type.State;
import com.antelif.library.factory.AuthorFactory;
import com.antelif.library.factory.BookFactory;
import com.antelif.library.factory.PublisherFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;

/** Helper class with all requests. */
public class RequestBuilder {

  private static final ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  /*
   * There are GET and POST methods that replicate calls to all controllers, so that depending on
   * the request we wish to mock, we can call one method and provide necessary arguments, instead of
   * mocking requests all the time.
   * GET: Depending on the request arguments must be provided, such as path variables or parameters.
   *      Finally, all GET methods call getRequest().
   * POST: Each method builds their needed request-body to include in request.
   *       Finally, all methods call postRequest().
   * PATCH: Each method builds their needed request body ton include in request and provides an endpoint.
   *        Finally, all methods call patchRequest()
   * ERROR: When asserting exception postRequestAndExpectError() and getRequestAndExpectError() are
   *        used so that the response can be mapped to an ErrorResponse without making such
   *        conversions in tests.
   */

  // GET
  @SneakyThrows
  public static String getRequest(String endpoint, MockMvc mockMvc) {
    String response =
        mockMvc
            .perform(get(endpoint).contentType(APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Object mapResponse = objectMapper.readValue(response, new TypeReference<>() {});
    return objectMapper.writeValueAsString(mapResponse);
  }

  @SneakyThrows
  public static List<BookResponse> getBooks(MockMvc mockMvc) {
    return objectMapper.readValue(
        getRequest(BOOKS_ENDPOINT, mockMvc), new TypeReference<List<BookResponse>>() {});
  }

  @SneakyThrows
  public static BookResponse getBookById(Long id, MockMvc mockMvc) {
    return objectMapper.readValue(
        getRequest(BOOKS_ENDPOINT + "/" + id, mockMvc), BookResponse.class);
  }

  @SneakyThrows
  public static List<PublisherResponse> getPublishers(MockMvc mockMvc) {
    return objectMapper.readValue(
        getRequest(PUBLISHERS_ENDPOINT, mockMvc), new TypeReference<>() {});
  }

  @SneakyThrows
  public static PublisherResponse getPublisherById(Long id, MockMvc mockMvc) {
    return objectMapper.readValue(
        getRequest(PUBLISHERS_ENDPOINT + "/" + id, mockMvc), PublisherResponse.class);
  }

  @SneakyThrows
  public static CustomerResponse getCustomerById(Long id, MockMvc mockMvc) {
    return objectMapper.readValue(
        getRequest(CUSTOMERS_ENDPOINT + "/" + id, mockMvc), CustomerResponse.class);
  }

  // POST
  @SneakyThrows
  private static String postRequest(String endpoint, String content, MockMvc mockMvc) {
    String response =
        mockMvc
            .perform(post(endpoint).contentType(APPLICATION_JSON).content(content))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Object mapResponse = objectMapper.readValue(response, new TypeReference<>() {});
    return objectMapper.writeValueAsString(mapResponse);
  }

  @SneakyThrows
  public static AuthorResponse postAuthor(AuthorRequest authorContent, MockMvc mockMvc) {
    Map<String, Object> authorMap =
        objectMapper.readValue(
            postRequest(AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class);
  }

  @SneakyThrows
  public static PublisherResponse postPublisher(
      PublisherRequest publisherContent, MockMvc mockMvc) {

    Map<String, Object> publisherMap =
        objectMapper.readValue(
            postRequest(
                PUBLISHERS_ENDPOINT, objectMapper.writeValueAsString(publisherContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class);
  }

  @SneakyThrows
  public static PersonnelResponse postPersonnel(
      PersonnelRequest personnelContent, MockMvc mockMvc) {
    Map<String, Object> personnelMap =
        objectMapper.readValue(
            postRequest(
                PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(personnelMap.get(CREATED)), PersonnelResponse.class);
  }

  @SneakyThrows
  public static CustomerResponse postCustomer(CustomerRequest customerContent, MockMvc mockMvc) {

    Map<String, Object> customerMap =
        objectMapper.readValue(
            postRequest(
                CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(customerMap.get(CREATED)), CustomerResponse.class);
  }

  @SneakyThrows
  public static BookCopyResponse postBookCopy(BookCopyRequest bookCopyContent, MockMvc mockMvc) {
    Map<String, Object> bookCopyMap =
        objectMapper.readValue(
            postRequest(
                BOOK_COPIES_ENDPOINT, objectMapper.writeValueAsString(bookCopyContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(bookCopyMap.get(CREATED)), BookCopyResponse.class);
  }

  @SneakyThrows
  public static BookResponse postBook(BookRequest bookContent, MockMvc mockMvc) {

    Map<String, Object> bookMap =
        objectMapper.readValue(
            postRequest(BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return objectMapper.readValue(
        objectMapper.writeValueAsString(bookMap.get(CREATED)), BookResponse.class);
  }

  @SneakyThrows
  public static BookResponse postBook(
      int bookIndex, int authorIndex, int publisherIndex, MockMvc mockMvc) {
    AuthorRequest authorRequest = AuthorFactory.createAuthorRequest(authorIndex);
    AuthorResponse authorResponse = postAuthor(authorRequest, mockMvc);

    PublisherRequest publisherRequest = PublisherFactory.createPublisherRequest(publisherIndex);
    PublisherResponse publisherResponse = postPublisher(publisherRequest, mockMvc);

    BookRequest bookRequest =
        BookFactory.createBookRequest(bookIndex, authorResponse.getId(), publisherResponse.getId());

    return postBook(bookRequest, mockMvc);
  }

  @SneakyThrows
  public static TransactionResponse postTransaction(
      TransactionRequest transactionContent, MockMvc mockMvc) {
    Map<String, Object> transactionMap =
        objectMapper.readValue(
            postRequest(
                TRANSACTIONS_ENDPOINT,
                objectMapper.writeValueAsString(transactionContent),
                mockMvc),
            new TypeReference<Map<String, Object>>() {});

    return objectMapper.readValue(
        objectMapper.writeValueAsString(transactionMap.get(CREATED)), TransactionResponse.class);
  }

  // PATCH
  @SneakyThrows
  public static List<TransactionResponse> patchTransactions(
      Long customerId, List<Long> bookCopyIds, MockMvc mockMvc) {
    Map<String, Object> transactionMap =
        objectMapper.readValue(
            patchRequest(
                TRANSACTIONS_ENDPOINT + "/customer/" + customerId, bookCopyIds.toString(), mockMvc),
            new TypeReference<Map<String, Object>>() {});

    return objectMapper.readValue(
        objectMapper.writeValueAsString(transactionMap.get(UPDATED)), new TypeReference<>() {});
  }

  @SneakyThrows
  public static CustomerResponse patchCustomerFee(Long customerId, Double fee, MockMvc mockMvc) {

    String response =
        mockMvc
            .perform(
                patch(CUSTOMERS_ENDPOINT + "/" + customerId).param("feeAmount", fee.toString()))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, CustomerResponse> customerMap =
        objectMapper.readValue(response, new TypeReference<Map<String, CustomerResponse>>() {});

    return objectMapper.readValue(
        objectMapper.writeValueAsString(customerMap.get(UPDATED)), CustomerResponse.class);
  }

  @SneakyThrows
  public static BookCopyResponse patchBookCopyState(Long bookCopyId, State state, MockMvc mockMvc) {

    BookCopyUpdateStateRequest bookCopyUpdateStateRequest = new BookCopyUpdateStateRequest();
    bookCopyUpdateStateRequest.setState(state);

    String response =
        mockMvc
            .perform(
                patch(BOOK_COPIES_ENDPOINT + "/" + bookCopyId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookCopyUpdateStateRequest)))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, BookCopyResponse> customerMap =
        objectMapper.readValue(response, new TypeReference<Map<String, BookCopyResponse>>() {});

    return objectMapper.readValue(
        objectMapper.writeValueAsString(customerMap.get(UPDATED)), BookCopyResponse.class);
  }

  @SneakyThrows
  public static TransactionResponse cancelTransaction(Long transactionId, MockMvc mockMvc) {

    String response =
        mockMvc
            .perform(patch(CANCEL_TRANSACTION_ENDPOINT + "/" + transactionId))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, Object> transactionMap =
        objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

    return objectMapper.readValue(
        objectMapper.writeValueAsString(transactionMap.get(CANCELLED)), new TypeReference<>() {});
  }

  @SneakyThrows
  private static String patchRequest(String endpoint, String content, MockMvc mockMvc) {
    String response =
        mockMvc
            .perform(
                patch(endpoint)
                    .characterEncoding(UTF_8)
                    .contentType(APPLICATION_JSON)
                    .content(content))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Object mapResponse = objectMapper.readValue(response, new TypeReference<>() {});
    return objectMapper.writeValueAsString(mapResponse);
  }

  // ERROR
  @SneakyThrows
  public static ErrorResponse postRequestAndExpectError(
      String endpoint, String content, MockMvc mockMvc) {
    return objectMapper.readValue(postRequest(endpoint, content, mockMvc), ErrorResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse getRequestAndExpectError(String endpoint, MockMvc mockMvc) {
    return objectMapper.readValue(getRequest(endpoint, mockMvc), ErrorResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse cancelTransactionAndExpectError(Long transactionId, MockMvc mockMvc) {
    String response =
        mockMvc
            .perform(patch(CANCEL_TRANSACTION_ENDPOINT + "/" + transactionId))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse patchCustomerFeeAndExpectError(
      Long customerId, Double fee, MockMvc mockMvc) {
    String response =
        mockMvc
            .perform(
                patch(CUSTOMERS_ENDPOINT + "/" + customerId).param("feeAmount", fee.toString()))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse patchBookCopyStateAndExpectError(
      Long bookCopyId, State state, MockMvc mockMvc) {

    BookCopyUpdateStateRequest bookCopyUpdateStateRequest = new BookCopyUpdateStateRequest();
    bookCopyUpdateStateRequest.setState(state);

    String response =
        mockMvc
            .perform(
                patch(BOOK_COPIES_ENDPOINT + "/" + bookCopyId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookCopyUpdateStateRequest)))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse patchRequestAndExpectError(
      String endpoint, String content, MockMvc mockMvc) {
    return objectMapper.readValue(patchRequest(endpoint, content, mockMvc), ErrorResponse.class);
  }
}
