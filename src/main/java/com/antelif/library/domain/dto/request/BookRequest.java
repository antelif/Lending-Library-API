package com.antelif.library.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

/** Book request DTO used as request body in HTTP requests. */
@Getter
@Setter
public class BookRequest {

  private String title;
  private String isbn;
  private long authorId;
//  private long publisherId;
}
