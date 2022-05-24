package com.antelif.library.integration.command;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;
import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.factory.BookFactory;
import com.antelif.library.factory.PublisherFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class BookCommandControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/books";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  private BookRequest bookRequest;
  private BookResponse bookExpectedResponse;
  private AuthorResponse author;
  private PublisherResponse publisher;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();

    bookCounter++;
    authorCounter++;
    publisherCounter++;

    author = createNewAuthorAndGetId(authorCounter);
    publisher = createNewPublisherAndGetId(publisherCounter);

    bookRequest =
        BookFactory.createBookRequest(
            bookCounter, Long.valueOf(author.getId()), Long.valueOf(publisher.getId()));
    bookExpectedResponse = BookFactory.createBookResponse(bookCounter, author, publisher);
  }

  @Test
  @DisplayName("Create successfully a book.")
  @SneakyThrows
  void testBookIsCreatedSuccessfully() {

    Map<String, BookResponse> response =
        objectMapper.readValue(createNewBook(bookRequest), new TypeReference<>() {});

    bookExpectedResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(bookExpectedResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create book fails when record exists for this isbn.")
  @SneakyThrows
  void testDuplicateBookIsNotCreated() {

    // Create first book
    createNewBook(bookRequest);

    // Same book creation should fail
    var response = createNewBook(bookRequest);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_BOOK.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Create book fails when author does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenAuthorDoesNotExist() {

    // Set author id to a non-existing author.
    bookRequest.setAuthorId(authorCounter + 1);

    var response = objectMapper.readValue(createNewBook(bookRequest), ErrorResponse.class);

    assertEquals(AUTHOR_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Create book fails when publisher does not exist.")
  @SneakyThrows
  void testBookIsNotCreatedWhenPublisherDoesNotExist() {

    // Set author id to a non-existing author.
    bookRequest.setPublisherId(publisherCounter + 1);

    var response = objectMapper.readValue(createNewBook(bookRequest), ErrorResponse.class);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }

  @SneakyThrows
  private String createNewBook(BookRequest book) {
    var content = objectMapper.writeValueAsString(book);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @SneakyThrows
  private AuthorResponse createNewAuthorAndGetId(int authorCounter) {
    var authorRequest = AuthorDtoFactory.createAuthorRequest(authorCounter);
    var content = objectMapper.writeValueAsString(authorRequest);

    var authorResponse =
        this.mockMvc
            .perform(post("/library/authors").contentType(CONTENT_TYPE).content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper
        .readValue(authorResponse, new TypeReference<Map<String, AuthorResponse>>() {})
        .get(CREATED);
  }

  @SneakyThrows
  private PublisherResponse createNewPublisherAndGetId(int publisherCounter) {
    var publisherRequest = PublisherFactory.createPublisherRequest(publisherCounter);
    var content = objectMapper.writeValueAsString(publisherRequest);

    var authorResponse =
        this.mockMvc
            .perform(post("/library/publishers").contentType(CONTENT_TYPE).content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper
        .readValue(authorResponse, new TypeReference<Map<String, PublisherResponse>>() {})
        .get(CREATED);
  }
}
