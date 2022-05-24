package com.antelif.library.application.controller.command;

import com.antelif.library.domain.dto.request.TransactionRequest;
import com.antelif.library.domain.service.TransactionService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** TransactionEntity Command Controller. */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transactions")
public class TransactionCommandController {

  private final TransactionService transactionService;

  /**
   * Receives POST requests for the creation of a new transaction.
   *
   * @param transaction the new object to persist in database.
   * @return a map with the transaction id as value.
   */
  @PostMapping
  public Map<String, Long> createTransaction(@RequestBody TransactionRequest transaction) {

    return Map.of("created", transactionService.createTransaction(transaction));
  }
}
