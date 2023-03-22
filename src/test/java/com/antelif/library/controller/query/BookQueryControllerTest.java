package com.antelif.library.controller.query;

import static com.antelif.library.application.error.GenericError.BOOK_DOES_NOT_EXIST;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.getBooks;
import static com.antelif.library.utils.RequestBuilder.getRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.factory.BookFactory;
import com.antelif.library.config.BaseIT;
import com.antelif.library.utils.RequestBuilder;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Books query controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
public class BookQueryControllerTest extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private BookResponse expectedBookResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    bookCounter++;
    authorCounter++;
    publisherCounter++;

    expectedBookResponse =
        BookFactory.createBookResponse(authorCounter, publisherCounter, bookCounter);
  }

  @Test
  @DisplayName("Book: Retrieve all books")
  @SneakyThrows
  void testRetrieveAllBooksSuccessfully() {
    postBook(bookCounter, authorCounter, publisherCounter, this.mockMvc);

    List<BookResponse> actualResponse = getBooks(this.mockMvc);

    assertTrue(0 < actualResponse.size());
  }

  @Test
  @DisplayName("Book: Retrieve book by id successfully.")
  @SneakyThrows
  void testRetrieveBookById() {
    Long bookId = postBook(bookCounter, authorCounter, publisherCounter, this.mockMvc).getId();

    BookResponse actualResponse = RequestBuilder.getBookById(bookId, this.mockMvc);

    assertNotNull(actualResponse.getId());
    assertEquals(expectedBookResponse.getTitle(), actualResponse.getTitle());
    assertEquals(expectedBookResponse.getIsbn(), actualResponse.getIsbn());

    assertNotNull(actualResponse.getAuthor().getId());
    assertEquals(expectedBookResponse.getAuthor().getName(), actualResponse.getAuthor().getName());
    assertEquals(
        expectedBookResponse.getAuthor().getMiddleName(),
        actualResponse.getAuthor().getMiddleName());
    assertEquals(
        expectedBookResponse.getAuthor().getSurname(), actualResponse.getAuthor().getSurname());

    assertNotNull(actualResponse.getPublisher().getId());
    assertEquals(
        expectedBookResponse.getPublisher().getName(), actualResponse.getPublisher().getName());
  }

  @Test
  @DisplayName("Book: Exception is thrown when retrieving a book that does not exist.")
  @SneakyThrows
  void testExceptionIsThrownWhenBookDoesNotExist() {

    long inexistentBookId = 9999L;

    ErrorResponse response = getRequestAndExpectError(BOOKS_ENDPOINT + "/" + inexistentBookId, this.mockMvc);

    assertEquals(BOOK_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
