package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_AUTHOR;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.AuthorFactory.createAuthorResponse;
import static com.antelif.library.utils.Request.postAuthor;
import static com.antelif.library.utils.Request.postAuthorWithoutMiddleName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.AuthorResponse;
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
class AuthorCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private AuthorResponse expectedAuthorResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;

    expectedAuthorResponse = createAuthorResponse(authorCounter);
  }

  @Test
  @DisplayName("Author: Successful creation with all arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithAllArguments() {

    var responseMap =
        objectMapper.readValue(
            postAuthor(authorCounter, this.mockMvc), new TypeReference<Map<String, Object>>() {});
    var actualAuthorResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(responseMap.get(CREATED)), AuthorResponse.class);

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

    var responseMap =
        objectMapper.readValue(
            postAuthorWithoutMiddleName(authorCounter, this.mockMvc),
            new TypeReference<Map<String, Object>>() {});
    var actualAuthorResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(responseMap.get(CREATED)), AuthorResponse.class);

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
    postAuthor(authorCounter, this.mockMvc);

    // Same author creation should fail
    var response = postAuthor(authorCounter, this.mockMvc);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Author: Unsuccessful creation when record exists for this name and surname.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {

    // Create first author
    postAuthorWithoutMiddleName(authorCounter, this.mockMvc);

    // Same author without middle name should fail
    var response = postAuthorWithoutMiddleName(authorCounter, this.mockMvc);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }
}
