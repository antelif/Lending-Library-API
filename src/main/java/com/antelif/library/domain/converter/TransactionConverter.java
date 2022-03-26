package com.antelif.library.domain.converter;

import com.antelif.library.domain.Transaction;
import com.antelif.library.domain.dto.TransactionDto;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.repository.CustomerRepository;
import com.antelif.library.infrastructure.repository.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** TransactionEntity and TransactionDto converter. */
@RequiredArgsConstructor
@Component
public class TransactionConverter
    implements Converter<Transaction, TransactionDto, TransactionEntity> {

  private final CustomerRepository customerRepository;
  private final PersonnelRepository personnelRepository;
  private final ModelMapper modelMapper;

  @Override
  public Transaction convertFromDtoToDomain(TransactionDto transactionDto) {
    return modelMapper.map(transactionDto, Transaction.class);
  }

  @Override
  public TransactionEntity convertFromDomainToEntity(Transaction transaction) {
    return modelMapper.map(transaction, TransactionEntity.class);
  }

  @Override
  public Transaction convertFromEntityToDomain(TransactionEntity transactionEntity) {
    return modelMapper.map(transactionEntity, Transaction.class);
  }

  @Override
  public TransactionDto convertFromDomainToDto(Transaction transaction) {
    return modelMapper.map(transaction, TransactionDto.class);
  }
}
