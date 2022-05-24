package com.antelif.library.domain.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** TransactionEntity DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class TransactionRequest {

  private int daysUntilReturn;
  private long customerId;
  private long personnelId;
}
