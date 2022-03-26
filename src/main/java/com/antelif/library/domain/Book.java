package com.antelif.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Book domain object. */
@Setter
@Getter
@EqualsAndHashCode
public class Book {

  private String isbn;
  private String title;
  private Author author;
  private Publisher publisher;
}
