package com.antelif.library.controller.command;

import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyResponse;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static com.antelif.library.utils.RequestBuilder.postBookCopy;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Book copies command controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
class BookCopyCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private BookCopyResponse expectedBookCopyResponse;

  private BookCopyRequest bookCopyRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {

    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    authorCounter++;
    publisherCounter++;
    bookCounter++;

    expectedBookCopyResponse = createBookCopyResponse(authorCounter, publisherCounter, bookCounter);

    var authorRequest = createAuthorRequest(authorCounter);
    var authorResponse = postAuthor(authorRequest, this.mockMvc);

    var publisherRequest = createPublisherRequest(publisherCounter);
    var publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    var bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    // Set book isbn
    var bookResponse = postBook(bookRequest, this.mockMvc);

    bookCopyRequest = createBookCopyRequest(bookResponse.getIsbn());
  }

  @Test
  @SneakyThrows
  @DisplayName("BookCopy: Successful creation.")
  void testNewBookCopyIsCreated() {

    var actualBookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

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
}
