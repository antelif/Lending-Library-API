package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** TransactionEntity and TransactionDto converter. */
@RequiredArgsConstructor
@Component
public class TransactionConverter
    implements Converter<TransactionRequest, TransactionEntity, TransactionResponse> {

  private final ModelMapper modelMapper;

  @Override
  public TransactionEntity convertFromRequestToEntity(TransactionRequest transactionRequest) {
    return modelMapper.map(transactionRequest, TransactionEntity.class);
  }

  @Override
  public TransactionResponse convertFromEntityToResponse(TransactionEntity transactionEntity) {
    return modelMapper.map(transactionEntity, TransactionResponse.class);
  }
}
