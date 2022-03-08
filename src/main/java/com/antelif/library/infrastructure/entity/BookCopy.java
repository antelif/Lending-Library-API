package com.antelif.library.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopy extends Book {

  private State state;
  private LendingStatus lendingStatus;
  private Long id;
}
