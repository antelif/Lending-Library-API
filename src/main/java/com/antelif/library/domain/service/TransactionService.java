package com.antelif.library.domain.service;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.factory.ConverterFactory;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.repository.TransactionRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Transaction service. */
@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final ConverterFactory converterFactory;

  /**
   * Add a new transaction in the database.
   *
   * @param transactionRequest contains information about the new transaction.
   * @return the transaction id.
   */
  @Transactional
  public Long createTransaction(TransactionRequest transactionRequest) {
    return transactionRepository
        .save(
            (TransactionEntity)
                converterFactory.getConverter(transactionRequest.getClass().toString()).stream()
                    .findAny()
                    .map(c -> c.convertFromRequestToEntity(transactionRequest))
                    // TODO: Replace with customized exception.
                    .orElseThrow(RuntimeException::new))
        .getId();
  }
}
