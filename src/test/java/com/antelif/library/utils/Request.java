package com.antelif.library.utils;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;

/** Helper class with all requests. */
public class Request {

  static ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  protected static String postRequest(String endpoint, String content, MockMvc mockMvc) {
    var response =
        mockMvc
            .perform(post(endpoint).contentType(APPLICATION_JSON).content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var mapResponse = objectMapper.readValue(response, new TypeReference<>() {});
    return objectMapper.writeValueAsString(mapResponse);
  }

  @SneakyThrows
  public static String postAuthor(String authorContent, MockMvc mockMvc) {
    return postRequest(AUTHORS_ENDPOINT, authorContent, mockMvc);
  }

  @SneakyThrows
  public static String postPublisher(String publisherContent, MockMvc mockMvc) {
    return postRequest(PUBLISHERS_ENDPOINT, publisherContent, mockMvc);
  }

  @SneakyThrows
  public static String postPersonnel(String personnelContent, MockMvc mockMvc) {
    return postRequest(PERSONNEL_ENDPOINT, personnelContent, mockMvc);
  }

  @SneakyThrows
  public static String postCustomer(String customerContent, MockMvc mockMvc) {
    return postRequest(CUSTOMERS_ENDPOINT, customerContent, mockMvc);
  }

  @SneakyThrows
  public static String postBookCopy(String bookCopyContent, MockMvc mockMvc) {
    return postRequest(BOOK_COPIES_ENDPOINT, bookCopyContent, mockMvc);
  }

  @SneakyThrows
  public static String postBook(String bookContent, MockMvc mockMvc) {

    return postRequest(BOOKS_ENDPOINT, bookContent, mockMvc);
  }

  @SneakyThrows
  public static String postTransaction(String transactionContent, MockMvc mockMvc) {
    return postRequest(TRANSACTIONS_ENDPOINT, transactionContent, mockMvc);
  }
}
