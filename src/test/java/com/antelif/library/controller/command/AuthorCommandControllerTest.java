package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_AUTHOR;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
class AuthorCommandControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/authors";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private AuthorRequest authorRequest;
  private AuthorResponse authorExpectedResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();
    modelMapper = new ModelMapper();

    authorCounter++;

    authorRequest = AuthorDtoFactory.createAuthorRequest(authorCounter);
    authorExpectedResponse = modelMapper.map(authorRequest, AuthorResponse.class);
  }

  @Test
  @DisplayName("Create successfully author with all arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameSurnameAndMiddleName() {

    Map<String, AuthorResponse> response =
        objectMapper.readValue(createNewAuthor(authorRequest), new TypeReference<>() {});

    authorExpectedResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(authorExpectedResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create successfully author with name and surname arguments.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameAndSurname() {

    Map<String, AuthorResponse> response =
        objectMapper.readValue(createNewAuthor(authorRequest), new TypeReference<>() {});

    authorExpectedResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(authorExpectedResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create author fails when record exists for this name, surname and middle name.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreated() {

    // Create first author
    createNewAuthor(authorRequest);

    // Same author creation should fail
    var response = createNewAuthor(authorRequest);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName("Create author fails when record exists for this name and surname.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {

    // Create first author
    createNewAuthor(authorRequest);

    var newAuthor = AuthorDtoFactory.createAuthorRequestNoMiddleName(authorCounter);

    // Same author without middle name should fail
    var response = createNewAuthor(newAuthor);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_AUTHOR.getCode(), errorResponse.getCode());
  }

  @SneakyThrows
  private String createNewAuthor(AuthorRequest author) {
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
