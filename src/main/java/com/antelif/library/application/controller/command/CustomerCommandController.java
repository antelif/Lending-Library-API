package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Constants.UPDATED;
import static com.antelif.library.domain.common.ControllerTags.CUSTOMER_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;

import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.service.CustomerService;
import io.swagger.annotations.Api;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer command controller.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = CUSTOMER_CONTROLLER)
@RequestMapping(value = CUSTOMERS_ENDPOINT)
public class CustomerCommandController {

  private final CustomerService customerService;

  /**
   * Add new customer endpoint.
   *
   * @param customerRequest the DTO to get information to create the new customer.
   * @return a customer response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, CustomerResponse>> addCustomer(
      @RequestBody @Valid CustomerRequest customerRequest) {
    log.info("Received request to add customer {}", customerRequest);
    return ResponseEntity.ok(Map.of(CREATED, customerService.addCustomer(customerRequest)));
  }

  /**
   * Updates the fee of the customer.
   *
   * @param customerId the customer id whose fee to update,
   * @param feeAmount  the fee amount to repay,
   * @return a customer response DTO.
   */
  @PatchMapping("/{customerId}")
  public ResponseEntity<Map<String, CustomerResponse>> updateCustomerFee(
      @PathVariable("customerId") Long customerId, Double feeAmount) {

    log.info("Received request to update fee for customer {} and repay {}", customerId, feeAmount);

    return ResponseEntity.ok(
        Map.of(UPDATED, customerService.updateCustomerFee(customerId, feeAmount)));
  }
}
