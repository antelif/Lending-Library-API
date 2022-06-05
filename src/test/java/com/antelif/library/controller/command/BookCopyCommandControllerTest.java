package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.BOOK_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyResponse;
import static com.antelif.library.utils.Request.postBook;
import static com.antelif.library.utils.Request.postBookCopy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.testcontainers.shaded.org.awaitility.Awaitility;

@SpringBootTest
@AutoConfigureMockMvc
class BookCopyCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private BookCopyResponse expectedBookCopyResponse;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;
    publisherCounter++;
    bookCounter++;

    expectedBookCopyResponse = createBookCopyResponse(authorCounter, publisherCounter, bookCounter);
  }

  @Test
  @SneakyThrows
  @DisplayName("BookCopy: Successful creation.")
  void testNewBookCopyIsCreated() {

    var bookResponseMap =
        objectMapper.readValue(
            postBook(authorCounter, publisherCounter, bookCounter, this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var bookResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(bookResponseMap.get(CREATED)), BookResponse.class);

    Awaitility.await().until(() -> !bookResponse.getId().isEmpty());

    var bookCopyResponseMap =
        objectMapper.readValue(
            postBookCopy(bookResponse.getIsbn(), this.mockMvc),
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
    var errorResponse =
        objectMapper.readValue(postBookCopy("RANDOM_ISBN", this.mockMvc), ErrorResponse.class);
    assertEquals(BOOK_DOES_NOT_EXIST.getCode(), errorResponse.getCode());
  }
}
