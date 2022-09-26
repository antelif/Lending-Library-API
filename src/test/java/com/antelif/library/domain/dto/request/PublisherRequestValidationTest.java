package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
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

@DisplayName("Validations Publisher")
public class PublisherRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private PublisherRequest publisherRequest;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    publisherCounter++;
    publisherRequest = createPublisherRequest(publisherCounter);
  }

  @Test
  @DisplayName("Publisher Validations: Publisher name cannot be empty.")
  @SneakyThrows
  void testPublisherNameCannotBeEmpty() {
    publisherRequest.setName(" ");

    var response =
        RequestBuilder.postRequestAndExpectError(
            AUTHORS_ENDPOINT, objectMapper.writeValueAsString(publisherRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
