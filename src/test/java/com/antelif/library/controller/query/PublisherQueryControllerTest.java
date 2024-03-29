package com.antelif.library.controller.query;

import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;
import static com.antelif.library.configuration.Roles.ADMIN;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.utils.Constants.ROOT_PASSWORD;
import static com.antelif.library.utils.Constants.ROOT_USER;
import static com.antelif.library.utils.RequestBuilder.getPublisherById;
import static com.antelif.library.utils.RequestBuilder.getPublishers;
import static com.antelif.library.utils.RequestBuilder.getRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.config.BaseIT;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Publishers query controller")
@WithMockUser(username = ROOT_USER, password = ROOT_PASSWORD, roles = ADMIN)
public class PublisherQueryControllerTest extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private PublisherRequest publisherRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

    publisherCounter++;
    publisherRequest = createPublisherRequest(publisherCounter);
  }

  @Test
  @DisplayName("Publisher: Retrieve all publishers")
  @SneakyThrows
  void testRetrieveAllPublishers() {
    postPublisher(publisherRequest, this.mockMvc);

    List<PublisherResponse> actualResponse = getPublishers(this.mockMvc);

    assertTrue(0 < actualResponse.size());
  }

  @Test
  @DisplayName("Publisher: Retrieve publisher by id successfully.")
  @SneakyThrows
  void testRetrievePublisherById() {
    Long publisherId = postPublisher(publisherRequest, this.mockMvc).getId();

    PublisherResponse actualResponse = getPublisherById(publisherId, this.mockMvc);

    assertNotNull(actualResponse.getId());
    assertEquals(publisherRequest.getName(), actualResponse.getName());
  }

  @Test
  @DisplayName("Publisher: Exception is thrown when retrieving a publisher that does not exist.")
  @SneakyThrows
  void testExceptionIsThrownWhenPublisherDoesNotExist() {

    long inexistentPublisherId = 9999L;

    ErrorResponse response =
        getRequestAndExpectError(PUBLISHERS_ENDPOINT + "/" + inexistentPublisherId, this.mockMvc);

    assertEquals(PUBLISHER_DOES_NOT_EXIST.getCode(), response.getCode());
  }
}
