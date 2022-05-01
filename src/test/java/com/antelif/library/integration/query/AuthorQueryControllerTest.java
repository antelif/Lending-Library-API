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
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorQueryControllerTest extends BaseIntegrationTest {

  @Autowired MockMvc mockMvc;

  private ObjectMapper objectMapper;

  private final String ENDPOINT = "/library/authors";
  private final String CONTENT_TYPE = "application/json";

  @BeforeEach
  void setUp() {
    this.objectMapper = new ObjectMapper();
    authorCounter++;
  }

  @Test
  @SneakyThrows
  @DisplayName("Author is retrieved by id successfully.")
  void getAuthorById_isSuccessful() {
    var author = AuthorDtoFactory.createAuthorDto(authorCounter);
    var response =
        objectMapper.readValue(
            createNewAuthor(author).getResponse().getContentAsString(), Map.class);
    var authorId = response.get(CREATED);

    var actualResponse =
        objectMapper.readValue(getAuthorById(String.valueOf(authorId)), AuthorDto.class);

    assertEquals(author, actualResponse);
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
    var author = AuthorDtoFactory.createAuthorDto(authorCounter);

    createNewAuthor(author);

    List<AuthorDto> actualResponse =
        objectMapper.readValue(
            getAuthorBySurname(author.getSurname()), new TypeReference<List<AuthorDto>>() {});

    assertEquals(1, actualResponse.size());
    assertEquals(author, actualResponse.get(0));
  }

  @Test
  @SneakyThrows
  @DisplayName("No authors are retrieved if there are not any with this surname.")
  void getAuthorsBySurname_returnsEmpty_ifThereNoAuthorsForSurname() {
    var author = AuthorDtoFactory.createAuthorDto(authorCounter);

    createNewAuthor(author);

    List<AuthorDto> actualResponse =
        objectMapper.readValue(
            getAuthorBySurname("wrongSurname"), new TypeReference<List<AuthorDto>>() {});

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
  private MvcResult createNewAuthor(AuthorDto author) {
    var content = objectMapper.writeValueAsString(author);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
  }
}
