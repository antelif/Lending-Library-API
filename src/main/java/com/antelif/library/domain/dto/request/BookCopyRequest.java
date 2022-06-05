package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.Status.SHELF;

import com.antelif.library.domain.type.State;
import com.antelif.library.domain.type.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Book copy request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class BookCopyRequest {
  private String isbn;
  private State state;
  private Status status = SHELF;
}
