package com.antelif.library.domain.dto.response;

import com.antelif.library.domain.type.BookCopyStatus;
import com.antelif.library.domain.type.State;
import lombok.Getter;
import lombok.Setter;

/**
 * Book copy response DTO used as response body in HTTP requests.
 */
@Getter
@Setter
public class BookCopyResponse {

  private Long id;
  private BookResponse book;
  private State state;
  private BookCopyStatus status;
}
