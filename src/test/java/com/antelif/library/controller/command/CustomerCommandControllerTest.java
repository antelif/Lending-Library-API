package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerResponse;
import static com.antelif.library.utils.RequestBuilder.postCustomer;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.integration.BaseIntegrationTest;
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
@DisplayName("Customer command controller")
class CustomerCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private CustomerResponse expectedCustomerResponse;

  private CustomerRequest customerRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    customerCounter++;

    expectedCustomerResponse = createCustomerResponse(customerCounter);
    customerRequest = createCustomerRequest(customerCounter);
  }

  @Test
  @DisplayName("Customer: Successful creation.")
  @SneakyThrows
  void testNewCustomerIsCreated() {

    var actualCustomer = postCustomer(customerRequest, this.mockMvc);

    assertNotNull(actualCustomer);

    assertNotNull(actualCustomer.getId());
    assertEquals(expectedCustomerResponse.getName(), actualCustomer.getName());
    assertEquals(expectedCustomerResponse.getSurname(), actualCustomer.getSurname());
    assertEquals(expectedCustomerResponse.getEmail(), actualCustomer.getEmail());
    assertEquals(expectedCustomerResponse.getFee(), actualCustomer.getFee());
    assertEquals(expectedCustomerResponse.getPhoneNo(), actualCustomer.getPhoneNo());
  }

  @Test
  @DisplayName("Customer: Unsuccessful creation when phone number exists.")
  @SneakyThrows
  void testCustomerIsNotCreatedWhenDuplicatePhoneNumber() {

    // Create first customer
    postCustomer(customerRequest, this.mockMvc);

    // Same customer creation should fail
    var errorResponse =
        postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(DUPLICATE_CUSTOMER.getCode(), errorResponse.getCode());
  }
}
