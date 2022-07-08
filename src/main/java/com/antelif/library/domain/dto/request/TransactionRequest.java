package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;

import com.antelif.library.domain.type.TransactionStatus;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Transaction request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class TransactionRequest {

  @NotEmpty(message = "Days to return a book shuould not be empty.")
  @Size(min = 1, max = 7, message = "Days to return a book should be between 1 and 10.")
  private int daysUntilReturn;

  @NotBlank(message = "Customer id should not be blank.")
  private long customerId;

  @NotBlank(message = "Personnel id should not be blank.")
  private long personnelId;

  private TransactionStatus status = ACTIVE;

  @NotEmpty(message = "Transaction should contain book copy ids.")
  private List<Long> copyIds;
}
