package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;

import com.antelif.library.domain.type.TransactionStatus;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Transaction request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class TransactionRequest {

  private int daysUntilReturn;
  private long customerId;
  private long personnelId;
  private TransactionStatus status = ACTIVE;
  private List<Long> copyIds;
}
