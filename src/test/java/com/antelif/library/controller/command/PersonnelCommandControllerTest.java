package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.factory.PersonnelFactory;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest
@AutoConfigureMockMvc
class PersonnelCommandControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/personnel";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private PersonnelRequest personnelRequest;
  private PersonnelResponse personnelResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();
    modelMapper = new ModelMapper();

    personnelCounter++;

    personnelRequest = PersonnelFactory.createPersonnelRequest(personnelCounter);
    personnelResponse = modelMapper.map(personnelRequest, PersonnelResponse.class);
  }

  @Test
  @DisplayName("Create successfully personnel.")
  @SneakyThrows
  void testNewPersonnelIsCreated() {

    Map<String, PersonnelResponse> response =
        objectMapper.readValue(createPersonnel(personnelRequest), new TypeReference<>() {});

    personnelResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(personnelResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create personnel fails when username exists.")
  @SneakyThrows
  void testPersonnelIsNotCreatedWhenDuplicateUsername() {

    // Create first personnel
    createPersonnel(personnelRequest);

    // Same personnel creation should fail
    var response = createPersonnel(personnelRequest);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_PERSONNEL.getCode(), errorResponse.getCode());
  }

  @SneakyThrows
  private String createPersonnel(PersonnelRequest personnel) {
    var content = objectMapper.writeValueAsString(personnel);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }
}
