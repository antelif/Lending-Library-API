package com.antelif.library.integration.command.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Create Author with all arguments successfully.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameSurnameAndMiddleName() {
    var author = AuthorDtoFactory.createAuthorDto(1);

    var response = createNewAuthor(author);

    assertEquals(
        objectMapper.writeValueAsString(author), response.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("Create Author with name and surname arguments successfully.")
  @SneakyThrows
  void testNewAuthorIsCreatedWithNameAndSurname() {
    var author = AuthorDtoFactory.createAuthorDtoNoMiddleName(2);

    var response = createNewAuthor(author);

    assertEquals(
        objectMapper.writeValueAsString(author), response.getResponse().getContentAsString());
  }

  @Test
  @DisplayName(
      "Create Author when name, when record exists for this name, surname and middle name fails.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreated() {
    var author = AuthorDtoFactory.createAuthorDto(3);

    // Create first author
    createNewAuthor(author);

    // Same author creation should fail
    Exception exception = assertThrows(Exception.class, () -> createNewAuthor(author));
  }

  @Test
  @DisplayName("Create Author when name, when record exists for this name and surname fails.")
  @SneakyThrows
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {
    var author = AuthorDtoFactory.createAuthorDto(4);

    createNewAuthor(author);

    var newAuthor = AuthorDtoFactory.createAuthorDtoNoMiddleName(4);

    Exception exception = assertThrows(Exception.class, () -> createNewAuthor(newAuthor));
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
