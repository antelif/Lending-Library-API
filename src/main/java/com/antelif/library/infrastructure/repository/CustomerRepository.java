package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.CustomerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CustomerEntity repository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  /**
   * Retrieve a customer by their by id.
   *
   * @param id the id of the customer.
   * @return a customer entity object if exists.
   */
  Optional<CustomerEntity> getCustomerById(Long id);

  /**
   * Retrieve a customer by their by phone number.
   *
   * @param phone the phone number of the customer.
   * @return a customer entity object if exists.
   */
  Optional<CustomerEntity> getCustomerByPhoneNo(String phone);
}
