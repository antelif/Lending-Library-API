package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PUBLISHER;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherResponse;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Publishers command controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
class PublisherCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private PublisherResponse expectedPublisherResponse;
  private PublisherRequest publisherRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    publisherCounter++;

    expectedPublisherResponse = createPublisherResponse(publisherCounter);
    publisherRequest = createPublisherRequest(publisherCounter);
  }

  @Test
  @DisplayName("Publisher: Successful creation.")
  @SneakyThrows
  void testNewPublisherIsCreatedSuccessfully() {

    var actualPublisherResponse = postPublisher(publisherRequest, this.mockMvc);

    assertNotNull(actualPublisherResponse);
    assertNotNull(actualPublisherResponse.getId());
    assertEquals(expectedPublisherResponse.getName(), actualPublisherResponse.getName());
  }

  @Test
  @DisplayName("Publisher: Unsuccessful creation when record exists for this name.")
  @SneakyThrows
  void testDuplicatePublisherIsNotCreated() {

    // Create first publisher
    postPublisher(publisherRequest, this.mockMvc);

    // Same publisher creation should fail
    var errorResponse =
        postRequestAndExpectError(
            PUBLISHERS_ENDPOINT, objectMapper.writeValueAsString(publisherRequest), this.mockMvc);

    assertEquals(DUPLICATE_PUBLISHER.getCode(), errorResponse.getCode());
  }
}
