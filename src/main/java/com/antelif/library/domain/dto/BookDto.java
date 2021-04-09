package com.antelif.library.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * BookEntity DTO.
 */
@Getter
@Setter
public class BookDto {

  private String title;
  private String isbn;
  private long authorId;
  private long publisherId;
  private List<BookCopyDto> copies;
}
