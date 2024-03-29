package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;
import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.factory.BookFactory;
import com.antelif.library.config.BaseIT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Books command controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
class BookCommandControllerIT extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private BookResponse expectedBookResponse;
  private BookRequest bookRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    objectMapper = new ObjectMapper();

    bookCounter++;
    authorCounter++;
    publisherCounter++;

    expectedBookResponse =
        BookFactory.createBookResponse(authorCounter, publisherCounter, bookCounter);

    AuthorRequest authorRequest = createAuthorRequest(authorCounter);
    AuthorResponse authorResponse = postAuthor(authorRequest, this.mockMvc);

    PublisherRequest publisherRequest = createPublisherRequest(publisherCounter);
    PublisherResponse publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    bookRequest = createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());
  }

  @Test
  @DisplayName("Book: Successful creation.")
  @SneakyThrows
  void testBookIsCreatedSuccessfully() {

    BookResponse actualBookResponse = postBook(bookRequest, this.mockMvc);

    assertNotNull(actualBookResponse);

    assertNotNull(actualBookResponse.getId());
    assertEquals(expectedBookResponse.getTitle(), actualBookResponse.getTitle());
    assertEquals(expectedBookResponse.getIsbn(), actualBookResponse.getIsbn());

    assertNotNull(actualBookResponse.getAuthor().getId());
    assertEquals(
        expectedBookResponse.getAuthor().getName(), actualBookResponse.getAuthor().getName());
    assertEquals(
        expectedBookResponse.getAuthor().getMiddleName(),
        actualBookResponse.getAuthor().getMiddleName());
    assertEquals(
        expectedBookResponse.getAuthor().getSurname(), actualBookResponse.getAuthor().getSurname());

    assertNotNull(actualBookResponse.getPublisher().getId());
    assertEquals(
        expectedBookResponse.getPublisher().getName(), actualBookResponse.getPublisher().getName());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when record exists for this isbn.")
  @SneakyThrows
  void testDuplicateBookIsNotCreated() {

    // Create first book
    postBook(bookRequest, this.mockMvc);

    // Same book creation should fail
    ErrorResponse response =
        postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(DUPLICATE_BOOK.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when author does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenAuthorDoesNotExist() {

    bookRequest.setAuthorId(authorCounter + 1L);

    ErrorResponse response =
        postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(AUTHOR_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when publisher does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenPublisherDoesNotExist() {

    bookRequest.setPublisherId(publisherCounter + 1L);

    ErrorResponse response =
        postRequestAndExpectError(
            BOOKS_ENDPOINT, objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
