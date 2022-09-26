package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;

import com.antelif.library.domain.type.TransactionStatus;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

/** Transaction request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class TransactionRequest {

  @Range(min = 1, max = 7, message = "Days to return a book should be between 1 and 10.")
  private Integer daysUntilReturn = 5;

  @NotNull(message = "Customer id should not be blank.")
  private Long customerId;

  @NotNull(message = "Personnel id should not be blank.")
  private Long personnelId;

  private TransactionStatus status = ACTIVE;

  @NotEmpty(message = "Transaction should contain book copy ids.")
  private List<Long> copyIds;
}
