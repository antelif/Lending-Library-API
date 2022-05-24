package com.antelif.library.domain.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Book DTO. */
@Getter
@Setter
public class BookRequest {

  private String title;
  private String isbn;
  private long authorId;
  private long publisherId;
  private List<BookCopyRequest> copies;
}
