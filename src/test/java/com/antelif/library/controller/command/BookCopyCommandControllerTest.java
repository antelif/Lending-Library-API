package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyResponse;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.Request.postAuthor;
import static com.antelif.library.utils.Request.postBook;
import static com.antelif.library.utils.Request.postBookCopy;
import static com.antelif.library.utils.Request.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Book copies command controller")
class BookCopyCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private BookCopyResponse expectedBookCopyResponse;

  private BookCopyRequest bookCopyRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;
    publisherCounter++;
    bookCounter++;

    expectedBookCopyResponse = createBookCopyResponse(authorCounter, publisherCounter, bookCounter);

    var authorRequest = createAuthorRequest(authorCounter);
    var publisherRequest = createPublisherRequest(publisherCounter);

    // Set author id
    var authorMap =
        objectMapper.readValue(
            postAuthor(objectMapper.writeValueAsString(authorRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var authorId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(authorMap.get(CREATED)), AuthorResponse.class)
            .getId();

    // Set publisher id
    var publisherMap =
        objectMapper.readValue(
            postPublisher(objectMapper.writeValueAsString(publisherRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var publisherId =
        objectMapper
            .readValue(
                objectMapper.writeValueAsString(publisherMap.get(CREATED)), PublisherResponse.class)
            .getId();

    var bookRequest = createBookRequest(bookCounter, authorId, publisherId);

    // Set book isbn
    var bookMap =
        objectMapper.readValue(
            postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var isbn =
        objectMapper
            .readValue(objectMapper.writeValueAsString(bookMap.get(CREATED)), BookResponse.class)
            .getIsbn();

    bookCopyRequest = createBookCopyRequest(isbn);
  }

  @Test
  @SneakyThrows
  @DisplayName("BookCopy: Successful creation.")
  void testNewBookCopyIsCreated() {

    var bookCopyResponseMap =
        objectMapper.readValue(
            postBookCopy(objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var actualBookCopyResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(bookCopyResponseMap.get(CREATED)),
            BookCopyResponse.class);

    assertNotNull(actualBookCopyResponse);

    assertNotNull(actualBookCopyResponse.getId());
    assertEquals(expectedBookCopyResponse.getState(), actualBookCopyResponse.getState());
    assertEquals(expectedBookCopyResponse.getStatus(), actualBookCopyResponse.getStatus());

    assertNotNull(actualBookCopyResponse.getBook().getId());
    assertEquals(
        expectedBookCopyResponse.getBook().getIsbn(), actualBookCopyResponse.getBook().getIsbn());
    assertEquals(
        expectedBookCopyResponse.getBook().getTitle(), actualBookCopyResponse.getBook().getTitle());

    assertNotNull(actualBookCopyResponse.getBook().getAuthor().getId());
    assertEquals(
        expectedBookCopyResponse.getBook().getAuthor().getName(),
        actualBookCopyResponse.getBook().getAuthor().getName());
    assertEquals(
        expectedBookCopyResponse.getBook().getAuthor().getMiddleName(),
        actualBookCopyResponse.getBook().getAuthor().getMiddleName());
    assertEquals(
        expectedBookCopyResponse.getBook().getAuthor().getSurname(),
        actualBookCopyResponse.getBook().getAuthor().getSurname());

    assertNotNull(actualBookCopyResponse.getBook().getPublisher().getId());
    assertEquals(
        expectedBookCopyResponse.getBook().getPublisher().getName(),
        actualBookCopyResponse.getBook().getPublisher().getName());
  }

  @Test
  @SneakyThrows
  @DisplayName("BookCopy: Unsuccessful creation when isbn does not exist.")
  void testNewBookCopyIsNotCreatedWhenIsbnDoesNotExist() {
    bookCopyRequest.setIsbn("RANDOM_ISBN");

    var errorResponse =
        objectMapper
            .readValue(
                postBookCopy(objectMapper.writeValueAsString(bookCopyRequest), this.mockMvc),
                new TypeReference<List<ErrorResponse>>() {})
            .get(0);
    assertEquals(BOOK_DOES_NOT_EXIST.getCode(), errorResponse.getCode());
  }
}
