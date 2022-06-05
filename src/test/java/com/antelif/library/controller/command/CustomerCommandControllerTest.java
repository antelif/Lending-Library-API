package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;
import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.factory.CustomerFactory.createCustomerResponse;
import static com.antelif.library.utils.Request.postCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.response.CustomerResponse;
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
class CustomerCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private CustomerResponse expectedCustomerResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    customerCounter++;

    expectedCustomerResponse = createCustomerResponse(customerCounter);
  }

  @Test
  @DisplayName("Customer: Successful creation.")
  @SneakyThrows
  void testNewCustomerIsCreated() {

    Map<String, CustomerResponse> customerResponseMap =
        objectMapper.readValue(
            postCustomer(customerCounter, this.mockMvc), new TypeReference<>() {});

    var actualCustomer =
        objectMapper.readValue(
            objectMapper.writeValueAsString(customerResponseMap.get(CREATED)),
            CustomerResponse.class);

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
    postCustomer(customerCounter, this.mockMvc);

    // Same customer creation should fail
    var errorResponse =
        objectMapper.readValue(postCustomer(customerCounter, this.mockMvc), ErrorResponse.class);
    assertEquals(DUPLICATE_CUSTOMER.getCode(), errorResponse.getCode());
  }
}