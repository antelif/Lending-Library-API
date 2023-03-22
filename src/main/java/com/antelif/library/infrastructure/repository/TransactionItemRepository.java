package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.TransactionItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TransactionItemEntity repository.
 */
@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItemEntity, Long> {

  /**
   * Gets an TransactionItemEntity object by the book copy id and the transaction id.
   *
   * @param bookCopyId    the id of the book copy,
   * @param transactionId the id of the transaction.
   * @return a TransactionItemEntityor an empty optional if there is no record for given ids.
   */
  Optional<TransactionItemEntity> getByBookCopy_IdAndTransaction_Id(
      Long bookCopyId, Long transactionId);

  /**
   * Get all TransactionItemEntity objects for the provided book copy id.
   *
   * @param bookCopyId the id of the book copy of the TransactionItemEntity objects.
   * @return a list of TransactionItemEntity or an empty list if there is no record with this book
   * copy id.
   */
  List<TransactionItemEntity> getByBookCopy_Id(Long bookCopyId);

  /**
   * Get all TransactionItemEntity objects for the provided transaction id.
   *
   * @param transactionId the id of the transaction of the TransactionItemEntity objects.
   * @return a list of TransactionItemEntity or an empty list if there is no record with this
   * transaction id.
   */
  List<TransactionItemEntity> getByTransaction_Id(Long transactionId);
}
