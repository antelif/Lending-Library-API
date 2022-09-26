package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_AUTHOR;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.AuthorFactory.createAuthorResponse;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Authors command controller")
class AuthorCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private AuthorResponse expectedAuthorResponse;
  private AuthorRequest authorRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;

    expectedAuthorResponse = createAuthorResponse(authorCounter);
    authorRequest = createAuthorRequest(authorCounter);
  }

  @Test
  @DisplayName("Author: Successful creation with all arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithAllArguments() {

    var actualAuthorResponse = postAuthor(authorRequest, this.mockMvc);

    assertNotNull(actualAuthorResponse);

    assertNotNull(actualAuthorResponse.getId());
    assertEquals(expectedAuthorResponse.getName(), actualAuthorResponse.getName());
    assertEquals(expectedAuthorResponse.getSurname(), actualAuthorResponse.getSurname());
    assertEquals(expectedAuthorResponse.getMiddleName(), actualAuthorResponse.getMiddleName());
  }

  @Test
  @DisplayName("Author: Successful creation with name and surname arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameAndSurname() {

    authorRequest.setMiddleName(null);

    var actualAuthorResponse = postAuthor(authorRequest, this.mockMvc);
    assertNotNull(actualAuthorResponse);

    assertNotNull(actualAuthorResponse.getId());
    assertEquals(expectedAuthorResponse.getName(), actualAuthorResponse.getName());
    assertEquals(expectedAuthorResponse.getSurname(), actualAuthorResponse.getSurname());
    assertNull(actualAuthorResponse.getMiddleName());
  }

  @Test
  @DisplayName(
      "Author: Unsuccessful creation when record exists for this name, surname and middle name.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreated() {

    // Create first author
    postAuthor(authorRequest, this.mockMvc);

    // Same author creation should fail
    var response =
        postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(DUPLICATE_AUTHOR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author: Unsuccessful creation when record exists for this name and surname.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {

    authorRequest.setMiddleName(null);

    // Create first author
    postAuthor(authorRequest, this.mockMvc);

    // Same author without middle name should fail
    var response =
        postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(DUPLICATE_AUTHOR.getCode(), response.getCode());
  }
}
