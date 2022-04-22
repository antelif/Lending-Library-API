package com.antelif.library.integration.query;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.factory.AuthorDtoFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorQueryControllerTest extends BaseIntegrationTest {

  @Autowired MockMvc mockMvc;

  private final String ENDPOINT = "/library/authors";
  private final String CONTENT_TYPE = "application/json";

  @Test
  @DisplayName("Author is retrieved by id successfully.")
  void getAuthorById_isSuccessful() {
    // Place an author
    var author = AuthorDtoFactory.createAuthorDto(5);

  }

  @Test
  @DisplayName("No author is retrieved by id, if id does not exist.")
  void getAuthorById_throwsException() {}

  @Test
  @DisplayName("Author are retrieved by surname successfully.")
  void getAuthorsBySurname_isSuccessful() {}

  @Test
  @DisplayName("No authors are retrieved if there are not any with this surname.")
  void getAuthorsBySurname_returnsEmpty_ifThereNoAuthorsForSurname() {}

  @SneakyThrows
  private MvcResult getAuthorById(Long id) {

    return this.mockMvc
        .perform(get(ENDPOINT).param("id", String.valueOf(id)))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
  }

  @SneakyThrows
  private MvcResult getAuthorBySurname(String surname) {
    return this.mockMvc
        .perform(get(ENDPOINT).param("surname", String.valueOf(surname)))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
  }
}
