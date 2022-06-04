package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;
import static com.antelif.library.domain.common.Constants.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antelif.library.application.error.ErrorResponse;
import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.factory.CustomerFactory;
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
class CustomerCommandControllerTest extends BaseIntegrationTest {

  private final String ENDPOINT = "/library/customers";
  private final String CONTENT_TYPE = "application/json";

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private ModelMapper modelMapper;

  private CustomerRequest customerRequest;
  private CustomerResponse customerResponse;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    objectMapper = new ObjectMapper();
    modelMapper = new ModelMapper();

    customerCounter++;

    customerRequest = CustomerFactory.createCustomerRequest(customerCounter);
    customerResponse = modelMapper.map(customerRequest, CustomerResponse.class);
  }

  @Test
  @DisplayName("Create successfully customer.")
  @SneakyThrows
  void testNewCustomerIsCreated() {

    Map<String, CustomerResponse> response =
        objectMapper.readValue(createCustomer(customerRequest), new TypeReference<>() {});

    customerResponse.setId(response.get(CREATED).getId());

    JSONAssert.assertEquals(
        objectMapper.writeValueAsString(customerResponse),
        objectMapper.writeValueAsString(response.get(CREATED)),
        JSONCompareMode.STRICT);
  }

  @Test
  @DisplayName("Create customer fails when phone number exists.")
  @SneakyThrows
  void testCustomerIsNotCreatedWhenDuplicatePhoneNumber() {

    // Create first customer
    createCustomer(customerRequest);

    // Same customer creation should fail
    var response = createCustomer(customerRequest);
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);
    assertEquals(DUPLICATE_CUSTOMER.getCode(), errorResponse.getCode());
  }

  @SneakyThrows
  private String createCustomer(CustomerRequest customer) {
    var content = objectMapper.writeValueAsString(customer);
    return this.mockMvc
        .perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }
}
