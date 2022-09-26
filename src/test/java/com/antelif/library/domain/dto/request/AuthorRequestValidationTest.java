package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.integration.BaseIntegrationTest;
import com.antelif.library.utils.RequestBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Validations Author")
public class AuthorRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private AuthorRequest authorRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    authorCounter++;

    authorRequest = createAuthorRequest(authorCounter);
  }

  @Test
  @DisplayName("Author Validations: Author name cannot be blank.")
  @SneakyThrows
  void testAuthorNameCannotBeBlank() {
    authorRequest.setName(" ");
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author surname cannot be null.")
  @SneakyThrows
  void testAuthorNameCannotBeNull() {
    authorRequest.setName(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author surname cannot be blank.")
  @SneakyThrows
  void testAuthorSurnameCannotBeBlank() {
    authorRequest.setSurname(" ");
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author surname cannot be null.")
  @SneakyThrows
  void testAuthorSurnameCannotBeNull() {
    authorRequest.setSurname(null);
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author name cannot be more than 50 characters.")
  @SneakyThrows
  void testAuthorNameCannotBeMoreThan50Characters() {
    authorRequest.setName("111111111111111111111111111111111111111111111111111");
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author surname cannot be more than 50 characters.")
  @SneakyThrows
  void testAuthorSurnameCannotBeMoreThan50Characters() {
    authorRequest.setSurname("111111111111111111111111111111111111111111111111111");
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Author Validations: Author name cannot be more than 50 characters.")
  @SneakyThrows
  void testAuthorMiddleNameCannotBeMoreThan50Characters() {
    authorRequest.setMiddleName("111111111111111111111111111111111111111111111111111");
    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(authorRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
