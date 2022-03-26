package com.antelif.library.domain.service;

import com.antelif.library.domain.dto.TransactionDto;
import com.antelif.library.domain.factory.ConverterFactory;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.repository.TransactionRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final ConverterFactory converterFactory;

  @Transactional
  public Long createTransaction(TransactionDto transactionDto) {
    return transactionRepository
        .save(
            (TransactionEntity)
                converterFactory.getConverter(transactionDto.getClass().toString()).stream()
                    .findAny()
                    .map(c -> c.convertFromDtoToDomain(transactionDto))
                    // TODO: Replace with customized exception.
                    .orElseThrow(RuntimeException::new))
        .getId();
  }
}
