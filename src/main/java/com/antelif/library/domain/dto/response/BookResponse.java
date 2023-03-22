package com.antelif.library.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Book response DTO used as request body in HTTP requests.
 */
@Getter
@Setter
public class BookResponse {

  private Long id;
  private String title;
  private String isbn;
  private AuthorResponse author;
  private PublisherResponse publisher;
}
