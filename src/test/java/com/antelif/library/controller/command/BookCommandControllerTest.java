package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;
import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.utils.Request.postBook;
import static com.antelif.library.utils.Request.postBookWithExistingAuthor;
import static com.antelif.library.utils.Request.postBookWithExistingAuthorAndPublisher;
import static com.antelif.library.utils.Request.postBookWithExistingPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.factory.BookFactory;
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

@SpringBootTest
@AutoConfigureMockMvc
class BookCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private BookResponse expectedBookResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();

    bookCounter++;
    authorCounter++;
    publisherCounter++;

    expectedBookResponse =
        BookFactory.createBookResponse(authorCounter, publisherCounter, bookCounter);
  }

  @Test
  @DisplayName("Book: Successful creation.")
  @SneakyThrows
  void testBookIsCreatedSuccessfully() {

    var response =
        objectMapper.readValue(
            postBook(authorCounter, publisherCounter, bookCounter, this.mockMvc),
            new TypeReference<Map<String, Object>>() {});

    var actualBookResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(response.get(CREATED)), BookResponse.class);

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
    postBook(authorCounter, publisherCounter, bookCounter, this.mockMvc);

    // Same book creation should fail
    var response =
        postBookWithExistingAuthorAndPublisher(
            authorCounter, publisherCounter, bookCounter, this.mockMvc);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_BOOK.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when author does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenAuthorDoesNotExist() {

    var response =
        objectMapper.readValue(
            postBookWithExistingAuthor(
                authorCounter + 1, publisherCounter, bookCounter, this.mockMvc),
            ErrorResponse.class);

    assertEquals(AUTHOR_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when publisher does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenPublisherDoesNotExist() {

    var response =
        objectMapper.readValue(
            postBookWithExistingPublisher(
                authorCounter, publisherCounter + 1, bookCounter, this.mockMvc),
            ErrorResponse.class);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
