package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.TRANSACTION_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.TRANSACTION_DOES_NOT_EXIST;
import static com.antelif.library.domain.service.validation.TransactionValidationService.validateCancel;
import static com.antelif.library.domain.service.validation.TransactionValidationService.validateCreation;
import static com.antelif.library.domain.service.validation.TransactionValidationService.validateUpdate;
import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;

import com.antelif.library.domain.converter.TransactionConverter;
import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Transaction service. */
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionConverter transactionConverter;

  /**
   * Add a new transaction to database.
   *
   * @param transactionRequest the DTO to get information about the new transaction to create.
   * @return a transaction response DTO.
   */
  public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
    TransactionEntity transactionEntity = transactionConverter.convertFromRequestToEntity(transactionRequest);

    validateCreation(transactionEntity);

    // Change all book copy statuses to lend.
    transactionEntity.updateTransactionItems(transactionRequest.getCopyIds());

    return Optional.of(transactionEntity)
        .map(transactionRepository::save)
        .map(transactionConverter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(TRANSACTION_CREATION_FAILED));
  }

  /**
   * Gets a list of book copies to return and updates the book copy status, and if the transaction
   * can be finalized the status is set to 'FINALIZED' and all book copies are 'AVAILABLE'.
   *
   * @param bookCopyIds the ids of the book copies to return and update their statuses back to
   *     AVAILABLE.
   * @param customerId the customer that returns the books.
   * @return a list with the updated transaction after books are returned.
   */
  public List<TransactionResponse> updateTransactions(List<Long> bookCopyIds, String customerId) {
    List<TransactionEntity> transactionsToUpdate =
        transactionRepository.findAllByCustomerIdAndStatus(Long.valueOf(customerId), ACTIVE);

    validateUpdate(transactionsToUpdate, bookCopyIds);

    // Update returned book statuses.
    transactionsToUpdate.forEach(transaction -> transaction.updateTransactionItems(bookCopyIds));

    // Finalize all eligible transactions.
    transactionsToUpdate.stream()
        .filter(TransactionEntity::canBeFinalized)
        .forEach(TransactionEntity::finalizeTransaction);

    return transactionsToUpdate.stream()
        .map(transactionConverter::convertFromEntityToResponse)
        .collect(Collectors.toList());
  }

  public TransactionResponse cancelTransaction(Long transactionId) {
    Optional<TransactionEntity> persistedTransaction = transactionRepository.findById(transactionId);

    if (persistedTransaction.isEmpty()) {
      throw new EntityDoesNotExistException(TRANSACTION_DOES_NOT_EXIST);
    }

    validateCancel(persistedTransaction.get());

    persistedTransaction.ifPresent(TransactionEntity::cancelTransaction);

    return transactionConverter.convertFromEntityToResponse(persistedTransaction.get());
  }
}
