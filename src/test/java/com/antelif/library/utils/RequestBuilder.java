package com.antelif.library.utils;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOK_COPIES_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.dto.response.TransactionResponse;
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

  private static final ObjectMapper mapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @SneakyThrows
  private static String postRequest(String endpoint, String content, MockMvc mockMvc) {
    var response =
        mockMvc
            .perform(post(endpoint).contentType(APPLICATION_JSON).content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var mapResponse = mapper.readValue(response, new TypeReference<>() {});
    return mapper.writeValueAsString(mapResponse);
  }

  @SneakyThrows
  public static AuthorResponse postAuthor(AuthorRequest authorContent, MockMvc mockMvc) {
    var authorMap =
        mapper.readValue(
            postRequest(AUTHORS_ENDPOINT, mapper.writeValueAsString(authorContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(
        mapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class);
  }

  @SneakyThrows
  public static PublisherResponse postPublisher(
      PublisherRequest publisherContent, MockMvc mockMvc) {

    var publisherMap =
        mapper.readValue(
            postRequest(PUBLISHERS_ENDPOINT, mapper.writeValueAsString(publisherContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(
        mapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class);
  }

  @SneakyThrows
  public static PersonnelResponse postPersonnel(
      PersonnelRequest personnelContent, MockMvc mockMvc) {
    var personnelMap =
        mapper.readValue(
            postRequest(PERSONNEL_ENDPOINT, mapper.writeValueAsString(personnelContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(
        mapper.writeValueAsString(personnelMap.get(CREATED)), PersonnelResponse.class);
  }

  @SneakyThrows
  public static CustomerResponse postCustomer(CustomerRequest customerContent, MockMvc mockMvc) {

    var customerMap =
        mapper.readValue(
            postRequest(CUSTOMERS_ENDPOINT, mapper.writeValueAsString(customerContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(
        mapper.writeValueAsString(customerMap.get(CREATED)), CustomerResponse.class);
  }

  @SneakyThrows
  public static BookCopyResponse postBookCopy(BookCopyRequest bookCopyContent, MockMvc mockMvc) {
    var bookCopyMap =
        mapper.readValue(
            postRequest(BOOK_COPIES_ENDPOINT, mapper.writeValueAsString(bookCopyContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(
        mapper.writeValueAsString(bookCopyMap.get(CREATED)), BookCopyResponse.class);
  }

  @SneakyThrows
  public static BookResponse postBook(BookRequest bookContent, MockMvc mockMvc) {

    var bookMap =
        mapper.readValue(
            postRequest(BOOKS_ENDPOINT, mapper.writeValueAsString(bookContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});
    return mapper.readValue(mapper.writeValueAsString(bookMap.get(CREATED)), BookResponse.class);
  }

  @SneakyThrows
  public static TransactionResponse postTransaction(
      TransactionRequest transactionContent, MockMvc mockMvc) {
    var transactionMap =
        mapper.readValue(
            postRequest(
                TRANSACTIONS_ENDPOINT, mapper.writeValueAsString(transactionContent), mockMvc),
            new TypeReference<Map<String, Object>>() {});

    return mapper.readValue(
        mapper.writeValueAsString(transactionMap.get(CREATED)), TransactionResponse.class);
  }

  @SneakyThrows
  public static ErrorResponse postRequestAndExpectError(
      String endpoint, String content, MockMvc mockMvc) {
    return mapper.readValue(postRequest(endpoint, content, mockMvc), ErrorResponse.class);
  }
}
