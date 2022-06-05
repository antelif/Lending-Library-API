package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.CUSTOMER_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.CUSTOMER_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_CUSTOMER;

import com.antelif.library.domain.converter.CustomerConverter;
import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.repository.CustomerRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Customer Service. */
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerConverter converter;

  /**
   * Adds a customer to database.
   *
   * @param customerRequest the DTO to get information about the customer to create.
   * @return a customer response DTO.
   */
  @Transactional
  public CustomerResponse addCustomer(CustomerRequest customerRequest) {

    var persistedCustomer = customerRepository.getCustomerByPhoneNo(customerRequest.getPhoneNo());

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

    var persistedCustomer = customerRepository.getCustomerById(id);

    return persistedCustomer.orElseThrow(
        () -> new EntityDoesNotExistException(CUSTOMER_DOES_NOT_EXIST));
  }
}
