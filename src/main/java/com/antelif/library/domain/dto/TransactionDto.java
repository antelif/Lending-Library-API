package com.antelif.library.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** TransactionEntity DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class TransactionDto {

  private int daysUntilReturn;
  private long customerId;
  private long personnelId;
}
