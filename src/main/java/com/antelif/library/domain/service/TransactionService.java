package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.TRANSACTION_CREATION_FAILED;

import com.antelif.library.domain.converter.TransactionConverter;
import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.infrastructure.repository.TransactionRepository;
import java.util.Optional;
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
    return Optional.ofNullable(transactionConverter.convertFromRequestToEntity(transactionRequest))
        .map(transactionRepository::save)
        .map(transactionConverter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(TRANSACTION_CREATION_FAILED));
  }
}
