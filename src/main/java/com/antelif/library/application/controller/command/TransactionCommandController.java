package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.Constants.CANCELLED;
import static com.antelif.library.domain.common.Constants.UPDATED;
import static com.antelif.library.domain.common.ControllerTags.TRANSACTION_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.service.TransactionService;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** TransactionEntity Command Controller. */
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = TRANSACTION_CONTROLLER)
@RequestMapping(value = TRANSACTIONS_ENDPOINT)
public class TransactionCommandController {

  private final TransactionService transactionService;

  /**
   * Add a new transaction endpoint.
   *
   * @param transaction the DTO to get information to create the new transaction.
   * @return a transaction response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, TransactionResponse>> createTransaction(
      @RequestBody @Valid TransactionRequest transaction) {
    log.info("Received request to add transaction {}", transaction);
    return ResponseEntity.ok(Map.of(CREATED, transactionService.createTransaction(transaction)));
  }

  /**
   * Updates a customer transaction by returning book copies.
   *
   * @param customerId the customer that returns the books,
   * @param bookCopyIds the ids of the book copies that are returned,
   * @return a list of updated transaction response DTOs.
   */
  @PatchMapping("/customer/{customerId}")
  public ResponseEntity<Map<String, List<TransactionResponse>>> returnBooksAndUpdateTransactions(
      @PathVariable("customerId") String customerId, @RequestBody List<Long> bookCopyIds) {

    log.info(
        "Received request from customer {} to return book copies: {}",
        customerId,
        bookCopyIds.toString());

    return ResponseEntity.ok(
        Map.of(UPDATED, transactionService.updateTransactions(bookCopyIds, customerId)));
  }

  /**
   * Cancels a transaction.
   *
   * @param transactionId the id of the transaction to cancel.
   * @return the canceled transaction.
   */
  @PatchMapping("/cancel/{id}")
  public ResponseEntity<Map<String, TransactionResponse>> cancelTransaction(
      @PathVariable("id") Long transactionId) {
    log.info("received request to cancel transaction {}", transactionId);

    return ResponseEntity.ok(Map.of(CANCELLED, transactionService.cancelTransaction(transactionId)));
  }
}
