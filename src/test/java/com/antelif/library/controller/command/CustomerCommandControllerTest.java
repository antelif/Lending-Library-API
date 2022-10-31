package com.antelif.library.controller.command;

import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;
import static com.antelif.library.application.error.GenericError.INVALID_CUSTOMER_UPDATE_VALUE;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;
import static com.antelif.library.domain.common.InstantConversions.nowInstantToDays;
import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.BookCopyFactory.createBookCopyRequest;
import static com.antelif.library.factory.BookFactory.createBookRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerRequest;
import static com.antelif.library.factory.CustomerFactory.createCustomerResponse;
import static com.antelif.library.factory.PersonnelFactory.createPersonnelRequest;
import static com.antelif.library.factory.PublisherFactory.createPublisherRequest;
import static com.antelif.library.factory.TransactionFactory.createTransactionRequest;
import static com.antelif.library.utils.RequestBuilder.getCustomerById;
import static com.antelif.library.utils.RequestBuilder.patchCustomerFee;
import static com.antelif.library.utils.RequestBuilder.patchCustomerFeeAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postAuthor;
import static com.antelif.library.utils.RequestBuilder.postBook;
import static com.antelif.library.utils.RequestBuilder.postBookCopy;
import static com.antelif.library.utils.RequestBuilder.postCustomer;
import static com.antelif.library.utils.RequestBuilder.postPersonnel;
import static com.antelif.library.utils.RequestBuilder.postPublisher;
import static com.antelif.library.utils.RequestBuilder.postRequestAndExpectError;
import static com.antelif.library.utils.RequestBuilder.postTransaction;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.infrastructure.repository.CustomerRepository;
import com.antelif.library.infrastructure.repository.TransactionRepository;
import com.antelif.library.integration.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Customer command controller")
class CustomerCommandControllerTest extends BaseIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private TransactionRepository transactionRepository;

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

  @Test
  @DisplayName("Customer: Successful update of customer fee.")
  @SneakyThrows
  void testCustomerFeeIsUpdated() {
    var actualCustomer = postCustomer(customerRequest, this.mockMvc);

    // Add transaction
    authorCounter++;
    publisherCounter++;
    bookCounter++;
    customerCounter++;
    personnelCounter++;

    var authorRequest = createAuthorRequest(authorCounter);
    var authorResponse = postAuthor(authorRequest, this.mockMvc);

    var publisherRequest = createPublisherRequest(publisherCounter);
    var publisherResponse = postPublisher(publisherRequest, this.mockMvc);

    var bookRequest =
        createBookRequest(bookCounter, authorResponse.getId(), publisherResponse.getId());

    var bookResponse = postBook(bookRequest, this.mockMvc);
    var isbn = bookResponse.getIsbn();

    var bookCopyRequest = createBookCopyRequest(isbn);
    var bookCopyResponse = postBookCopy(bookCopyRequest, this.mockMvc);

    var personnelRequest = createPersonnelRequest(personnelCounter);
    var personnelResponse = postPersonnel(personnelRequest, this.mockMvc);

    var transactionRequest =
        createTransactionRequest(
            actualCustomer.getId(), personnelResponse.getId(), bookCopyResponse.getId());
    var transactionResponse = postTransaction(transactionRequest, this.mockMvc);

    // Update customer last update to be one day before so that we need to update them again
    var persistedCustomer = customerRepository.getCustomerById(actualCustomer.getId()).get();
    persistedCustomer.setLastUpdate(nowInstantToDays().minus(1, DAYS));
    customerRepository.save(persistedCustomer);

    // Update transaction return time to be 10 days late so that fee should be 5.
    var persistedTransaction = transactionRepository.findById(transactionResponse.getId()).get();
    persistedTransaction.setCreationDate(nowInstantToDays().minus(11, DAYS));
    persistedTransaction.setReturnDate(nowInstantToDays().minus(10, DAYS));
    transactionRepository.save(persistedTransaction);

    // Repay some of the fee
    var customerResponse = patchCustomerFee(actualCustomer.getId(), 3.0, this.mockMvc);

    // Retrieve customer again
    var updatedCustomer = getCustomerById(customerResponse.getId(), this.mockMvc);

    assertEquals(2, updatedCustomer.getFee());
  }

  @Test
  @DisplayName("Customer: Update of customer fee fails when return amount in negative.")
  @SneakyThrows
  void testCustomerUpdateFailsWhenReturnAmountIsNegative() {
    var actualCustomer = postCustomer(customerRequest, this.mockMvc);

    // Update customer with negative fee should fail.
    var errorResponse = patchCustomerFeeAndExpectError(actualCustomer.getId(), -10.0, this.mockMvc);

    assertEquals(INVALID_CUSTOMER_UPDATE_VALUE.getCode(), errorResponse.getCode());
  }

  @Test
  @DisplayName(
      "Customer: Update of customer fee fails when return amount in greater than customer fee.")
  @SneakyThrows
  void testCustomerUpdateFailsWhenReturnAmountIsGreaterThanCustomerFee() {
    var actualCustomer = postCustomer(customerRequest, this.mockMvc);

    // Update customer with negative fee should fail.
    var errorResponse = patchCustomerFeeAndExpectError(actualCustomer.getId(), 100.0, this.mockMvc);

    assertEquals(INVALID_CUSTOMER_UPDATE_VALUE.getCode(), errorResponse.getCode());
  }
}
