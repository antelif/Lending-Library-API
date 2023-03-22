package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.CUSTOMER_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;

import com.antelif.library.configuration.AppProperties;
import com.antelif.library.domain.converter.CustomerConverter;
import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.domain.service.validation.CustomerValidationService;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.repository.CustomerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Customer Service. */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerConverter converter;
  private final AppProperties appProperties;

  /**
   * Adds a customer to database.
   *
   * @param customerRequest the DTO to get information about the customer to create.
   * @return a customer response DTO.
   */
  public CustomerResponse addCustomer(CustomerRequest customerRequest) {

    Optional<CustomerEntity> persistedCustomer = customerRepository.getCustomerByPhoneNo(customerRequest.getPhoneNo());

    if (persistedCustomer.isPresent()) {
      throw new DuplicateEntityException(DUPLICATE_CUSTOMER);
    }

    return Optional.of(converter.convertFromRequestToEntity(customerRequest))
        .map(customerRepository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(CUSTOMER_CREATION_FAILED));
  }

  /**
   * Retrieve a customer from the database by provided id.
   *
   * @param id of the customer to retrieve.
   * @return a customer entity if customer was retrieved.
   */
  public CustomerEntity getCustomerById(Long id) {

    CustomerEntity persistedCustomer =
        customerRepository
            .getCustomerById(id)
            .orElseThrow(() -> new EntityDoesNotExistException(CUSTOMER_DOES_NOT_EXIST));

    persistedCustomer.updateFee(appProperties.getDailyFeeRate());

    return persistedCustomer;
  }

  /**
   * Updates the customer fee by subtracting the feeAmount provided from the customer fee.
   *
   * @param customerId the customer whose fee to update,
   * @param feeAmount the amount to subtract from the customer fee,
   * @return a customer response DTO.
   */
  public CustomerResponse updateCustomerFee(Long customerId, Double feeAmount) {

    CustomerEntity persistedCustomer =
        customerRepository
            .getCustomerById(customerId)
            .orElseThrow(() -> new EntityDoesNotExistException(CUSTOMER_DOES_NOT_EXIST));

    persistedCustomer.updateFee(appProperties.getDailyFeeRate());

    CustomerValidationService.validateUpdate(persistedCustomer, feeAmount);

    persistedCustomer.payFee(feeAmount);

    return converter.convertFromEntityToResponse(persistedCustomer);
  }

  /** Recalculates fee for all customers. */
  public void recalculateCustomerFee() {
    customerRepository
        .findAll()
        .forEach(customer -> customer.updateFee(appProperties.getDailyFeeRate()));
  }
}
