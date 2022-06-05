package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelResponse;
import static com.antelif.library.utils.Request.postPersonnel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class PersonnelCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private PersonnelResponse expectedPersonnelResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    personnelCounter++;
    expectedPersonnelResponse = createPersonnelResponse(personnelCounter);
  }

  @Test
  @DisplayName("Personnel: Successful creation.")
  @SneakyThrows
  void testNewPersonnelIsCreated() {

    var personnelResponseMap =
        objectMapper.readValue(
            postPersonnel(personnelCounter, this.mockMvc),
            new TypeReference<Map<String, Object>>() {});

    var actualPersonnelResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(personnelResponseMap.get(CREATED)),
            PersonnelResponse.class);

    assertNotNull(actualPersonnelResponse);

    assertNotNull(actualPersonnelResponse.getId());
    assertEquals(expectedPersonnelResponse.getUsername(), actualPersonnelResponse.getUsername());
  }

  @Test
  @DisplayName("Personnel: Unsuccessful creation when username exists.")
  @SneakyThrows
  void testPersonnelIsNotCreatedWhenDuplicateUsername() {

    // Create first personnel
    postPersonnel(personnelCounter, this.mockMvc);

    // Same personnel creation should fail
    var errorResponse =
        objectMapper.readValue(
            postPersonnel(personnelCounter, this.mockMvc), ErrorResponse.class);
    assertEquals(DUPLICATE_PERSONNEL.getCode(), errorResponse.getCode());
  }
}
