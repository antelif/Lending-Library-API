package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.integration.BaseIntegrationTest;
import com.antelif.library.utils.RequestBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Validations Book")
public class BookRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private BookRequest bookRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    bookCounter++;
    authorCounter++;
    publisherCounter++;
    bookRequest = createBookRequest(bookCounter, authorCounter, publisherCounter);
  }

  @Test
  @DisplayName("Book Validations: isbn cannot be blank.")
  @SneakyThrows
  void testBookIsbnCannotBeBlank() {
    bookRequest.setIsbn(" ");
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: isbn cannot be null.")
  @SneakyThrows
  void testBookIsbnCannotBeNull() {
    bookRequest.setIsbn(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: isbn should be of correct format.")
  @SneakyThrows
  @Disabled("Disabled until ISBN validations are concluded.")
  void testBookIsbnFormat() {
    bookRequest.setIsbn("1111");
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: title cannot be blank.")
  @SneakyThrows
  void testBookTitleCannotBeBlank() {
    bookRequest.setTitle(" ");
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: title cannot be null.")
  @SneakyThrows
  void testBookTitleCannotBeNull() {
    bookRequest.setTitle(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: title cannot be longer than 50 characters.")
  @SneakyThrows
  void testBookTitleCannotBeLongerThan50Characters() {
    bookRequest.setTitle("aaaaaaaaaaAAAAAAAAAAaaaaaaaaaaAAAAAAAAAAaaaaaaaaaaAAAAAAAAAA");
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: author cannot be null.")
  @SneakyThrows
  void testBookAuthorCannotBeNull() {
    bookRequest.setAuthorId(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Validations: publisher cannot be null.")
  @SneakyThrows
  void testBookPublisherCannotBeNull() {
    bookRequest.setPublisherId(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
