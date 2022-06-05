package com.antelif.library.utils;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.BOOK_COPIES_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequestNoMiddleName;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;

/** Helper class with all requests. */
public class Request {

  static ObjectMapper objectMapper = new ObjectMapper();

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

    var mapResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
    return objectMapper.writeValueAsString(mapResponse);
  }

  @SneakyThrows
  public static String postAuthor(int authorIndex, MockMvc mockMvc) {

    return postRequest(
        AUTHORS_ENDPOINT,
        objectMapper.writeValueAsString(createAuthorRequest(authorIndex)),
        mockMvc);
  }

  @SneakyThrows
  public static String postAuthorWithoutMiddleName(int authorIndex, MockMvc mockMvc) {

    return postRequest(
        AUTHORS_ENDPOINT,
        objectMapper.writeValueAsString(createAuthorRequestNoMiddleName(authorIndex)),
        mockMvc);
  }

  @SneakyThrows
  public static String postBook(
      int authorIndex, int publisherIndex, int bookIndex, MockMvc mockMvc) {

    // Add author
    var authorMap =
        objectMapper.readValue(
            postAuthor(authorIndex, mockMvc), new TypeReference<Map<String, Object>>() {});
    var authorResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class);

    await().until(() -> Optional.ofNullable(authorResponse).isPresent());

    // Add publisher
    var publisherMap =
        objectMapper.readValue(
            postPublisher(publisherIndex, mockMvc), new TypeReference<Map<String, Object>>() {});
    var publisherResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class);
    await().until(() -> Optional.ofNullable(publisherResponse).isPresent());

    // Add book
    return postRequest(
        BOOKS_ENDPOINT,
        objectMapper.writeValueAsString(
            createBookRequest(
                bookIndex,
                Long.parseLong(authorResponse.getId()),
                Long.parseLong(publisherResponse.getId()))),
        mockMvc);
  }

  @SneakyThrows
  public static String postBookWithExistingAuthorAndPublisher(
      int authorIndex, int publisherIndex, int bookIndex, MockMvc mockMvc) {

    return postRequest(
        BOOKS_ENDPOINT,
        objectMapper.writeValueAsString(createBookRequest(bookIndex, authorIndex, publisherIndex)),
        mockMvc);
  }

  @SneakyThrows
  public static String postBookWithExistingAuthor(
      int authorIndex, int publisherIndex, int bookIndex, MockMvc mockMvc) {

    // Add publisher
    var publisherMap =
        objectMapper.readValue(
            postPublisher(publisherIndex, mockMvc), new TypeReference<Map<String, Object>>() {});
    var publisherResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class);
    await().until(() -> publisherMap.size() > 0);

    return postRequest(
        BOOKS_ENDPOINT,
        objectMapper.writeValueAsString(
            createBookRequest(bookIndex, authorIndex, Long.parseLong(publisherResponse.getId()))),
        mockMvc);
  }

  @SneakyThrows
  public static String postBookWithExistingPublisher(
      int authorIndex, int publisherIndex, int bookIndex, MockMvc mockMvc) {

    // Add author
    var authorMap =
        objectMapper.readValue(
            postAuthor(authorIndex, mockMvc), new TypeReference<Map<String, Object>>() {});
    var authorResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class);

    await().until(() -> authorMap.size() > 0);

    return postRequest(
        BOOKS_ENDPOINT,
        objectMapper.writeValueAsString(
            createBookRequest(bookIndex, Long.parseLong(authorResponse.getId()), publisherIndex)),
        mockMvc);
  }

  @SneakyThrows
  public static String postBookCopy(
      int authorIndex, int publisherIndex, int bookIndex, MockMvc mockMvc) {

    var bookResponse =
        objectMapper.readValue(
            postBook(authorIndex, publisherIndex, bookIndex, mockMvc), BookResponse.class);

    var bookCopyResponse =
        postRequest(
            BOOK_COPIES_ENDPOINT,
            objectMapper.writeValueAsString(createBookCopyRequest(bookResponse.getId())),
            mockMvc);

    return objectMapper.writeValueAsString(
        objectMapper.readValue(bookCopyResponse, new TypeReference<Map<String, Object>>() {}));
  }

  @SneakyThrows
  public static String postBookCopy(String isbn, MockMvc mockMvc) {
    var bookCopyResponse =
        postRequest(
            BOOK_COPIES_ENDPOINT,
            objectMapper.writeValueAsString(createBookCopyRequest(isbn)),
            mockMvc);

    var responseMap =
        objectMapper.readValue(bookCopyResponse, new TypeReference<Map<String, Object>>() {});
    return objectMapper.writeValueAsString(responseMap);
  }

  @SneakyThrows
  public static String postCustomer(int customerIndex, MockMvc mockMvc) {
    var customerResponse =
        postRequest(
            CUSTOMERS_ENDPOINT,
            objectMapper.writeValueAsString(createCustomerRequest(customerIndex)),
            mockMvc);
    return objectMapper.writeValueAsString(
        objectMapper.readValue(customerResponse, new TypeReference<Map<String, Object>>() {}));
  }

  @SneakyThrows
  public static String postPersonnel(int personnelIndex, MockMvc mockMvc) {
    var personnelResponse =
        postRequest(
            PERSONNEL_ENDPOINT,
            objectMapper.writeValueAsString(createPersonnelRequest(personnelIndex)),
            mockMvc);

    return objectMapper.writeValueAsString(
        objectMapper.readValue(personnelResponse, new TypeReference<Map<String, Object>>() {}));
  }

  @SneakyThrows
  public static String postPublisher(int publisherIndex, MockMvc mockMvc) {
    var publisherResponse =
        postRequest(
            PUBLISHERS_ENDPOINT,
            objectMapper.writeValueAsString(createPublisherRequest(publisherIndex)),
            mockMvc);
    return objectMapper.writeValueAsString(
        objectMapper.readValue(publisherResponse, new TypeReference<Map<String, Object>>() {}));
  }
}
