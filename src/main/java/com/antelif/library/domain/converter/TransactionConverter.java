package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.TransactionDto;
import com.antelif.library.infrastructure.entity.Customer;
import com.antelif.library.infrastructure.entity.Transaction;
import com.antelif.library.infrastructure.repository.CustomerRepository;
import com.antelif.library.infrastructure.repository.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Transaction and TransactionDto converter. */
@RequiredArgsConstructor
@Component
public class TransactionConverter implements Converter<Transaction, TransactionDto> {

  private final CustomerRepository customerRepository;
  private final PersonnelRepository personnelRepository;

  @Override
  public Transaction convertToDomain(TransactionDto transactionDto) {

    var customer = customerRepository.getCustomerById(transactionDto.getCustomerId());

    if (customer.isEmpty()) {
      // TODO: Throw customized exception
      throw new RuntimeException();
    }
    if (!customer.map(Customer::canBorrow).stream().findAny().orElse(false)) {
      // TODO: Throw customized Exception
      throw new RuntimeException();
    }

    var personnel = personnelRepository.getPersonnelById(transactionDto.getPersonnelId());

    var transaction = new Transaction();
    transaction.setCustomer(customer.get());
    transaction.setPersonnel(personnel.get());

    return transaction;
  }

  @Override
  public TransactionDto convertToDto(Transaction transaction) {
    return null;
  }
}
