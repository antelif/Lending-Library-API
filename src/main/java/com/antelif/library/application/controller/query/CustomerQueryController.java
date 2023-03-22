package com.antelif.library.application.controller.query;

import static com.antelif.library.domain.common.ControllerTags.CUSTOMER_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.CUSTOMERS_ENDPOINT;

import com.antelif.library.domain.converter.CustomerConverter;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.service.CustomerService;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer query controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = CUSTOMER_CONTROLLER)
@RequestMapping(CUSTOMERS_ENDPOINT)
public class CustomerQueryController {

  private final CustomerService customerService;
  private final CustomerConverter customerConverter;

  /**
   * Retrieve a customer from the database by provided id.
   *
   * @param id of the customer to retrieve.
   * @return a publisher response object.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") Long id) {

    log.info("Received request to get customer {}", id);
    CustomerEntity customerEntity = customerService.getCustomerById(id);
    return ResponseEntity.ok(customerConverter.convertFromEntityToResponse(customerEntity));
  }
}
