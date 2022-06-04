package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PUBLISHER;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.factory.PublisherFactory;
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
class PublisherCommandControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/publishers";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private PublisherRequest publisherRequest;
  private PublisherResponse publisherResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();
    modelMapper = new ModelMapper();

    publisherCounter++;

    publisherRequest = PublisherFactory.createPublisherRequest(publisherCounter);
    publisherResponse = modelMapper.map(publisherRequest, PublisherResponse.class);
  }

  @Test
  @DisplayName("Create successfully publisher.")
  @SneakyThrows
  void testNewPublisherIsCreatedWithNameSurnameAndMiddleName() {

    Map<String, PublisherResponse> response =
        objectMapper.readValue(createPublisher(publisherRequest), new TypeReference<>() {});

    publisherResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(publisherResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create publisher fails when record exists for this name.")
  @SneakyThrows
  void testDuplicatePublisherIsNotCreated() {

    // Create first publisher
    createPublisher(publisherRequest);

    // Same publisher creation should fail
    var response = createPublisher(publisherRequest);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_PUBLISHER.getCode(), errorResponse.getCode());
  }

  @SneakyThrows
  private String createPublisher(PublisherRequest publisher) {
    var content = objectMapper.writeValueAsString(publisher);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }
}
