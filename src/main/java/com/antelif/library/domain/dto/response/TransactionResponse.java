package com.antelif.library.domain.dto.response;

import com.antelif.library.domain.type.TransactionStatus;
import java.time.Instant;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
  private Long id;
  private Instant creationDate;
  private Instant returnDate;
  private TransactionStatus status;
  private CustomerResponse customer;
  private PersonnelResponse personnel;
  private Set<BookCopyResponse> books;
}
