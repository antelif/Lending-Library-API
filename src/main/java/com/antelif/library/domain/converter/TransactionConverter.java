package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.service.BookCopyService;
import com.antelif.library.domain.service.CustomerService;
import com.antelif.library.domain.service.PersonnelService;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import com.antelif.library.infrastructure.entity.TransactionItemEntity;
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

  private final CustomerConverter customerConverter;
  private final PersonnelConverter personnelConverter;
  private final BookCopyConverter bookCopyConverter;

  @Override
  public TransactionEntity convertFromRequestToEntity(TransactionRequest transactionRequest) {
    var transaction = modelMapper.map(transactionRequest, TransactionEntity.class);

    var customer = customerService.getCustomerById(transactionRequest.getCustomerId());
    var personnel = personnelService.getPersonnelById(transactionRequest.getPersonnelId());
    var bookCopies = bookCopyService.getBookCopiesByBookCopyIds(transactionRequest.getCopyIds());

    transaction.setCustomer(customer);
    transaction.setPersonnel(personnel);
    transaction.setDates(transactionRequest.getDaysUntilReturn());
    transaction.addItems(transaction.createTransactionItemsOfCopies(bookCopies));

    return transaction;
  }

  @Override
  public TransactionResponse convertFromEntityToResponse(TransactionEntity transactionEntity) {
    var transaction = modelMapper.map(transactionEntity, TransactionResponse.class);

    transaction.setCustomer(
        customerConverter.convertFromEntityToResponse(transactionEntity.getCustomer()));

    transaction.setPersonnel(
        personnelConverter.convertFromEntityToResponse(transactionEntity.getPersonnel()));

    var bookCopies =
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
