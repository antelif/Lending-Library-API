package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.CustomerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** CustomerEntity repository. */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> getCustomerById(Long ig);
}
