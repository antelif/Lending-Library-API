package com.antelif.library.domain;

import com.antelif.library.domain.type.State;
import com.antelif.library.domain.type.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookCopy {

  private Book book;
  private State state;
  private Status status;
}
