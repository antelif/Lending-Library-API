package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelResponse;
import static com.antelif.library.utils.RequestBuilder.postPersonnel;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Personnel command controller")
class PersonnelCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private PersonnelResponse expectedPersonnelResponse;
  private PersonnelRequest personnelRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    personnelCounter++;
    expectedPersonnelResponse = createPersonnelResponse(personnelCounter);
    personnelRequest = createPersonnelRequest(personnelCounter);
  }

  @Test
  @DisplayName("Personnel: Successful creation.")
  @SneakyThrows
  void testNewPersonnelIsCreated() {

    var actualPersonnelResponse = postPersonnel(personnelRequest, this.mockMvc);

    assertNotNull(actualPersonnelResponse);

    assertNotNull(actualPersonnelResponse.getId());
    assertEquals(expectedPersonnelResponse.getUsername(), actualPersonnelResponse.getUsername());
  }

  @Test
  @DisplayName("Personnel: Unsuccessful creation when username exists.")
  @SneakyThrows
  void testPersonnelIsNotCreatedWhenDuplicateUsername() {

    // Create first personnel
    postPersonnel(personnelRequest, this.mockMvc);

    // Same personnel creation should fail
    var errorResponse =
        postRequestAndExpectError(
            PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelRequest), this.mockMvc);

    assertEquals(DUPLICATE_PERSONNEL.getCode(), errorResponse.getCode());
  }
}
