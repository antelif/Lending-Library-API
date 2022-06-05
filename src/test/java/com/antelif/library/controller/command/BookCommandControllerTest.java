package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;
import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.Request.postAuthor;
import static com.antelif.library.utils.Request.postBook;
import static com.antelif.library.utils.Request.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.factory.BookFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@DisplayName("Books command controller")
class BookCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private BookResponse expectedBookResponse;
  private BookRequest bookRequest;

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

    bookRequest = createBookRequest(bookCounter, authorId, publisherId);
  }

  @Test
  @DisplayName("Book: Successful creation.")
  @SneakyThrows
  void testBookIsCreatedSuccessfully() {

    var response =
        objectMapper.readValue(
            postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc),
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
    postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc);

    // Same book creation should fail
    var response = postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc);
    var errorResponse =
        objectMapper.readValue(response, new TypeReference<List<ErrorResponse>>() {}).get(0);
    assertEquals(DUPLICATE_BOOK.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when author does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenAuthorDoesNotExist() {

    bookRequest.setAuthorId(authorCounter + 1);

    var response =
        objectMapper
            .readValue(
                postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc),
                new TypeReference<List<ErrorResponse>>() {})
            .get(0);

    assertEquals(AUTHOR_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Book: Unsuccessful creation when publisher does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenPublisherDoesNotExist() {

    bookRequest.setPublisherId(publisherCounter + 1);

    var response =
        objectMapper
            .readValue(
                postBook(objectMapper.writeValueAsString(bookRequest), this.mockMvc),
                new TypeReference<List<ErrorResponse>>() {})
            .get(0);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
