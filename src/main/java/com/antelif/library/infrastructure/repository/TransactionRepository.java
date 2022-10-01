package com.antelif.library.infrastructure.repository;

import com.antelif.library.domain.type.TransactionStatus;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** TransactionEntity repository. */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  /**
   * Gets a list of TransactionEntity objects for a given customer and status
   *
   * @param customerId the customer to get transactions for,
   * @param status the status of the transactions to query.
   * @return a list of TransactionEntity objects.
   */
  List<TransactionEntity> findAllByCustomerIdAndStatus(Long customerId, TransactionStatus status);
}
