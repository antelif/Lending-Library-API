package com.antelif.library.domain.dto.request;

import com.antelif.library.domain.type.State;
import com.antelif.library.domain.type.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** BookCopyEntity DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class BookCopyRequest {
  private String isbn;
  private State state;
  private Status status;
}