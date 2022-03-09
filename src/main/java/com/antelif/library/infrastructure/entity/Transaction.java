package com.antelif.library.infrastructure.entity;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {

  private List<BookCopy> books;
  private Instant creationDate;
  private Instant returnDate;
  private Customer customer;
}
