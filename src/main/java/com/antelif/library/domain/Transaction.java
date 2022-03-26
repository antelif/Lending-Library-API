package com.antelif.library.domain;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Transaction domain object. */
@Getter
@Setter
@EqualsAndHashCode
public class Transaction {

  private Instant creationDate;
  private Instant returnDate;
  private Customer customer;
  private Personnel personnel;
}
