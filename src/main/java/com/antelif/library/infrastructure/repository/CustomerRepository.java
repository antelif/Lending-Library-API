package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Customer repository. */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> getCustomerById(Long ig);
}
