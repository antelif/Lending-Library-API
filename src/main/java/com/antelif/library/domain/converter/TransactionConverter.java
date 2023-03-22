package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.service.BookCopyService;
import com.antelif.library.domain.service.CustomerService;
import com.antelif.library.domain.service.PersonnelService;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import com.antelif.library.infrastructure.entity.PersonnelEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.entity.TransactionItemEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** Converter for transaction objects. */
@RequiredArgsConstructor
@Component
public class TransactionConverter
    implements Converter<TransactionRequest, TransactionEntity, TransactionResponse> {

  private final ModelMapper modelMapper;

  private final BookCopyService bookCopyService;
  private final CustomerService customerService;
  private final PersonnelService personnelService;

  private final BookCopyConverter bookCopyConverter;

  @Override
  public TransactionEntity convertFromRequestToEntity(TransactionRequest transactionRequest) {
    TransactionEntity transaction = modelMapper.map(transactionRequest, TransactionEntity.class);

    CustomerEntity customer = customerService.getCustomerById(transactionRequest.getCustomerId());
    PersonnelEntity personnel = personnelService.getPersonnelById(transactionRequest.getPersonnelId());
    List<BookCopyEntity> bookCopies = bookCopyService.getBookCopiesByBookCopyIds(transactionRequest.getCopyIds());

    transaction.setCustomer(customer);
    transaction.setPersonnel(personnel);
    transaction.setDates(transactionRequest.getDaysUntilReturn());
    transaction.addItems(transaction.createTransactionItemsOfCopies(bookCopies));

    return transaction;
  }

  @Override
  public TransactionResponse convertFromEntityToResponse(TransactionEntity transactionEntity) {
    TransactionResponse transaction = modelMapper.map(transactionEntity, TransactionResponse.class);

    Set<BookCopyEntity> bookCopies =
        transactionEntity.getTransactionItems().stream()
            .map(TransactionItemEntity::getBookCopy)
            .collect(Collectors.toSet());

    transaction.setBooks(
        bookCopies.stream()
            .map(bookCopyConverter::convertFromEntityToResponse)
            .collect(Collectors.toSet()));

    return transaction;
  }
}
