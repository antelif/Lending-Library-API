package com.antelif.library.integration.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_AUTHOR;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
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

  private final String ENDPOINT = "/library/authors";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    authorCounter++;
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Create successfully author with all arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameSurnameAndMiddleName() {
    var author = AuthorDtoFactory.createAuthorDto(authorCounter);

    var response = objectMapper.readValue(createNewAuthor(author), Map.class);

    var expectedResponse = Map.of(CREATED, authorCounter);
    assertEquals(expectedResponse, response);
  }

  @Test
  @DisplayName("Create successfully author with name and surname arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameAndSurname() {
    var author = AuthorDtoFactory.createAuthorDtoNoMiddleName(authorCounter);

    var response = objectMapper.readValue(createNewAuthor(author), Map.class);

    var expectedResponse = Map.of(CREATED, authorCounter);
    assertEquals(expectedResponse, response);
  }

  @Test
  @DisplayName("Create author fails when record exists for this name, surname and middle name.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreated() {
    var author = AuthorDtoFactory.createAuthorDto(3);

    // Create first author
    createNewAuthor(author);

    // Same author creation should fail
    var response = createNewAuthor(author);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Create author fails when record exists for this name and surname.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {

    // Create first author
    createNewAuthor(AuthorDtoFactory.createAuthorDto(authorCounter));

    var newAuthor = AuthorDtoFactory.createAuthorDtoNoMiddleName(authorCounter);

    // Same author without middle name should fail
    var response = createNewAuthor(newAuthor);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }

  @SneakyThrows
  private String createNewAuthor(AuthorDto author) {
    var content = objectMapper.writeValueAsString(author);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }
}
