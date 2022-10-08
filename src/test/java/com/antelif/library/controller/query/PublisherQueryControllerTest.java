package com.antelif.library.controller.query;

import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.RequestBuilder.getPublisherById;
import static com.antelif.library.utils.RequestBuilder.getPublishers;
import static com.antelif.library.utils.RequestBuilder.getRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Publishers query controller")
public class PublisherQueryControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private PublisherRequest publisherRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    publisherCounter++;
    publisherRequest = createPublisherRequest(publisherCounter);
  }

  @Test
  @DisplayName("Publisher: Retrieve all publishers")
  @SneakyThrows
  void testRetrieveAllPublishers() {
    postPublisher(publisherRequest, this.mockMvc);

    var actualResponse = getPublishers(this.mockMvc);

    assertTrue(0 < actualResponse.size());
  }

  @Test
  @DisplayName("Publisher: Retrieve publisher by id successfully.")
  @SneakyThrows
  void testRetrievePublisherById() {
    var publisherId = postPublisher(publisherRequest, this.mockMvc).getId();

    var actualResponse = getPublisherById(publisherId, this.mockMvc);

    assertNotNull(actualResponse.getId());
    assertEquals(publisherRequest.getName(), actualResponse.getName());
  }

  @Test
  @DisplayName("Publisher: Exception is thrown when retrieving a publisher that does not exist.")
  @SneakyThrows
  void testExceptionIsThrownWhenPublisherDoesNotExist() {

    var inexistentPublisherId = 9999L;

    var response =
        getRequestAndExpectError(PUBLISHERS_ENDPOINT + "/" + inexistentPublisherId, this.mockMvc);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
