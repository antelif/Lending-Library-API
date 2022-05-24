package com.antelif.library.integration.query;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Constants.SURNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import java.util.List;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorQueryControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/authors";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private AuthorRequest authorRequest;
  private AuthorResponse authorExpectedResponse;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.objectMapper = new ObjectMapper();
    this.modelMapper = new ModelMapper();

    authorCounter++;

    authorRequest = AuthorDtoFactory.createAuthorRequest(authorCounter);
    authorExpectedResponse = modelMapper.map(authorRequest, AuthorResponse.class);
  }

  @Test
  @SneakyThrows
  @DisplayName("Author is retrieved by id successfully.")
  void getAuthorById_isSuccessful() {

    Map<String, AuthorResponse> response =
        objectMapper.readValue(
            createNewAuthor(authorRequest), new TypeReference<Map<String, AuthorResponse>>() {});

    var authorId = response.get(CREATED).getId();

    authorExpectedResponse.setId(authorId);

    var actualResponse = objectMapper.readValue(getAuthorById(authorId), AuthorResponse.class);

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(authorExpectedResponse),
        objectMapper.writeValueAsString(actualResponse),
        JSONCompareMode.STRICT);
  }

  @Test
  @SneakyThrows
  @DisplayName("No author is retrieved by id, if id does not exist.")
  void getAuthorById_throwsException() {

    var actualResponse =
        objectMapper.readValue(
            getAuthorById(String.valueOf(authorCounter + 1)), ErrorResponse.class);

    assertEquals(AUTHOR_DOES_NOT_EXIST.getCode(), actualResponse.getCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Authors are retrieved by surname successfully.")
  void getAuthorsBySurname_isSuccessful() {
    createNewAuthor(authorRequest);

    List<AuthorResponse> actualResponse =
        objectMapper.readValue(
            getAuthorBySurname(authorRequest.getSurname()),
            new TypeReference<List<AuthorResponse>>() {});

    var authorId = actualResponse.get(0).getId();
    authorExpectedResponse.setId(authorId);

    assertEquals(1, actualResponse.size());
    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(authorExpectedResponse),
        objectMapper.writeValueAsString(actualResponse.get(0)),
        JSONCompareMode.STRICT);
  }

  @Test
  @SneakyThrows
  @DisplayName("No authors are retrieved if there are not any with this surname.")
  void getAuthorsBySurname_returnsEmpty_ifThereNoAuthorsForSurname() {
    createNewAuthor(authorRequest);

    List<AuthorRequest> actualResponse =
        objectMapper.readValue(getAuthorBySurname("wrongSurname"), new TypeReference<>() {});

    assertEquals(0, actualResponse.size());
  }

  @SneakyThrows
  private String getAuthorById(String id) {

    return this.mockMvc
        .perform(get(ENDPOINT + "/" + id))
        .andDo(print())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @SneakyThrows
  private String getAuthorBySurname(String surname) {
    return this.mockMvc
        .perform(get(ENDPOINT).param(SURNAME, String.valueOf(surname)))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
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
