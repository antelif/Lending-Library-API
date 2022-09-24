package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.ControllerTags.TRANSACTION_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.TRANSACTIONS_ENDPOINT;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.dto.response.TransactionResponse;
import com.antelif.library.domain.service.TransactionService;
import io.swagger.annotations.Api;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
      @RequestBody TransactionRequest transaction) {
    log.info("Received request to add transaction {}", transaction);
    return ResponseEntity.ok(Map.of(CREATED, transactionService.createTransaction(transaction)));
  }
}
