package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.PERSONNEL_ENDPOINT;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelResponse;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.postPersonnel;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.config.BaseIT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Personnel command controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
class PersonnelCommandControllerIT extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private PersonnelResponse expectedPersonnelResponse;
  private PersonnelRequest personnelRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    personnelCounter++;
    expectedPersonnelResponse = createPersonnelResponse(personnelCounter);
    personnelRequest = createPersonnelRequest(personnelCounter);
  }

  @Test
  @DisplayName("Personnel: Successful creation.")
  @SneakyThrows
  void testNewPersonnelIsCreated() {

    PersonnelResponse actualPersonnelResponse = postPersonnel(personnelRequest, this.mockMvc);

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
    ErrorResponse errorResponse =
        postRequestAndExpectError(
            PERSONNEL_ENDPOINT, objectMapper.writeValueAsString(personnelRequest), this.mockMvc);

    assertEquals(DUPLICATE_PERSONNEL.getCode(), errorResponse.getCode());
  }
}
