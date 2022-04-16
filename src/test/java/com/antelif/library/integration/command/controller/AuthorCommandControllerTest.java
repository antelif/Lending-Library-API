package com.antelif.library.integration.command.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
  }

  @Test
  @DisplayName("Create Author with all arguments successfully.")
  @SneakyThrows
  @DirtiesContext
  void testNewAuthorIsCreatedWithNameSurnameAndMiddleName() {
    var author = new AuthorDto();
    author.setName("Author_name_1");
    author.setSurname("Author_surname_1");
    author.setMiddleName("Author_middleName_1");

    var response = createNewAuthor(author);

    assertEquals(
        objectMapper.writeValueAsString(author), response.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("Create Author with name and surname arguments successfully.")
  @SneakyThrows
  @DirtiesContext
  void testNewAuthorIsCreatedWithNameAndSurname() {
    var author = new AuthorDto();
    author.setName("Author_name_4");
    author.setSurname("Author_surname_4");

    var response = createNewAuthor(author);

    assertEquals(
        objectMapper.writeValueAsString(author), response.getResponse().getContentAsString());
  }

  @Test
  @DisplayName(
      "Create Author when name, when record exists for this name, surname and middle name fails.")
  @SneakyThrows
  @DirtiesContext
  void testDuplicateAuthorIsNotCreated() {
    var author = new AuthorDto();
    author.setName("Author_name_2");
    author.setSurname("Author_surname_2");
    author.setMiddleName("Author_middleName_2");

    // Create first author
    createNewAuthor(author);

    // Same author creation should fail
    Exception exception = assertThrows(Exception.class, () -> createNewAuthor(author));
  }

  @Test
  @DisplayName("Create Author when name, when record exists for this name and surname fails.")
  @SneakyThrows
  @DirtiesContext
  void testDuplicateAuthorIsNotCreatedWhenGivingNameAndSurnameOnly() {
    var author = new AuthorDto();
    author.setName("Author_name_3");
    author.setSurname("Author_surname_3");
    author.setMiddleName("Author_middleName_3");

    createNewAuthor(author);

    var newAuthor = new AuthorDto();
    newAuthor.setName("Author_name_3");
    newAuthor.setSurname("Author_surname_3");

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
