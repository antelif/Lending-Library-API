package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
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

@DisplayName("Validations Personnel")
public class PersonnelRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private PersonnelRequest personnelRequest;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    personnelCounter++;
    personnelRequest = createPersonnelRequest(personnelCounter);
  }

  @Test
  @DisplayName("Personnel Validations: Personnel username cannot be empty.")
  @SneakyThrows
  void testPersonnelNameCannotBeEmpty() {
    personnelRequest.setUsername(" ");

    var response =
        RequestBuilder.postRequestAndExpectError(
            PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Personnel Validations: Personnel username cannot be longer than 20 characters.")
  @SneakyThrows
  void testPersonnelNameCannotBeLongerThan20Characters() {
    personnelRequest.setUsername("111111111111111111111");

    var response =
        RequestBuilder.postRequestAndExpectError(
            PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Personnel Validations: Personnel password cannot be empty.")
  @SneakyThrows
  void testPersonnelPasswordCannotBeEmpty() {
    personnelRequest.setPassword(" ");

    var response =
        RequestBuilder.postRequestAndExpectError(
            PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
