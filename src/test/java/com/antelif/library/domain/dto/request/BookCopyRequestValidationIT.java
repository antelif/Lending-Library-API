package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.BOOK_COPIES_ENDPOINT;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.config.BaseIT;
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

@DisplayName("Validations Book Copy")
public class BookCopyRequestValidationIT extends BaseIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private BookCopyRequest bookCopyRequest;
  private final String ISBN = "1234567890123";

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    bookCopyRequest = createBookCopyRequest(ISBN);
  }

  @Test
  @DisplayName("Book Copy Validations: isbn cannot be blank.")
  @SneakyThrows
  void testBookCopyIsbnCannotBeBlank() {
    bookCopyRequest.setIsbn(" ");
    ErrorResponse response =
        RequestBuilder.postRequestAndExpectError(
            BOOK_COPIES_ENDPOINT, objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Copy Validations: isbn cannot be null.")
  @SneakyThrows
  void testBookCopyIsbnCannotBeNull() {
    bookCopyRequest.setIsbn(null);
    ErrorResponse response =
        RequestBuilder.postRequestAndExpectError(
            BOOK_COPIES_ENDPOINT, objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Copy Validations: isbn should be of correct format.")
  @SneakyThrows
  @Disabled("Disabled until ISBN validations are concluded.")
  void testBookCopyIsbnFormat() {
    bookCopyRequest.setIsbn("1111");
    ErrorResponse response =
        RequestBuilder.postRequestAndExpectError(
            BOOK_COPIES_ENDPOINT, objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book Copy Validations: state cannot be blank.")
  @SneakyThrows
  void testBookCopyStateCannotBeBlank() {
    bookCopyRequest.setState(null);
    ErrorResponse response =
        RequestBuilder.postRequestAndExpectError(
            BOOK_COPIES_ENDPOINT, objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
