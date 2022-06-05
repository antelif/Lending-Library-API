package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PUBLISHER;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.PublisherFactory.createPublisherResponse;
import static com.antelif.library.utils.Request.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class PublisherCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private PublisherResponse expectedPublisherResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    publisherCounter++;

    expectedPublisherResponse = createPublisherResponse(publisherCounter);
  }

  @Test
  @DisplayName("Publisher: Successful creation.")
  @SneakyThrows
  void testNewPublisherIsCreatedSuccesfully() {

    var publisherResponseMap =
        objectMapper.readValue(
            postPublisher(publisherCounter, this.mockMvc),
            new TypeReference<Map<String, Object>>() {});

    var actualPublisherResponse =
        objectMapper.readValue(
            objectMapper.writeValueAsString(publisherResponseMap.get(CREATED)),
            PublisherResponse.class);

    assertNotNull(actualPublisherResponse);
    assertNotNull(actualPublisherResponse.getId());
    assertEquals(expectedPublisherResponse.getName(), actualPublisherResponse.getName());
  }

  @Test
  @DisplayName("Publisher: Unsuccessful creation when record exists for this name.")
  @SneakyThrows
  void testDuplicatePublisherIsNotCreated() {

    // Create first publisher
    postPublisher(publisherCounter, this.mockMvc);

    // Same publisher creation should fail
    var errorResponse =
        objectMapper.readValue(
            postPublisher(publisherCounter, this.mockMvc), ErrorResponse.class);
    assertEquals(DUPLICATE_PUBLISHER.getCode(), errorResponse.getCode());
  }
}
