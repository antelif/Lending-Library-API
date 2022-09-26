package com.antelif.library.domain.dto.request;

import static com.antelif.library.application.error.GenericError.INPUT_VALIDATIONS_ERROR;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
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

@DisplayName("Validations Customer")
public class CustomerRequestValidationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext webApplicationContext;

  private CustomerRequest customerRequest;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    customerCounter++;

    customerRequest = createCustomerRequest(customerCounter);
  }

  @Test
  @DisplayName("Customer name cannot be blank")
  @SneakyThrows
  void testCustomerNameCannotBeBlank() {
    customerRequest.setName(" ");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer name cannot be longer than 50 characters")
  @SneakyThrows
  void testCustomerNameCannotBeLongerThan50Characters() {
    customerRequest.setName("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer surname cannot be blank")
  @SneakyThrows
  void testCustomerSurnameCannotBeBlank() {
    customerRequest.setSurname(" ");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer surname cannot be longer than 50 characters")
  @SneakyThrows
  void testCustomerSurnameCannotBeLongerThan50Characters() {
    customerRequest.setSurname("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer phone number cannot contain character other than digits.")
  @SneakyThrows
  void testCustomerPhoneNoCannotContainCharacterOtherThanDigits() {
    customerRequest.setPhoneNo("aaaaaaaaaaa");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer phone number should contain more than 10 digits.")
  @SneakyThrows
  void testCustomerPhoneNoContainMoreThan10Digits() {
    customerRequest.setPhoneNo("123456");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer phone number should contain less than 15 digits.")
  @SneakyThrows
  void testCustomerPhoneNoContainLessThan15Digits() {
    customerRequest.setPhoneNo("1234567891011121314");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }

  @Test
  @DisplayName("Customer email should have email format.")
  @SneakyThrows
  void testCustomerEmailFormat() {
    customerRequest.setEmail("aa");

    var response =
        RequestBuilder.postRequestAndExpectError(
            CUSTOMERS_ENDPOINT, objectMapper.writeValueAsString(customerRequest), this.mockMvc);

    assertEquals(INPUT_VALIDATIONS_ERROR.getCode(), response.getCode());
  }
}
