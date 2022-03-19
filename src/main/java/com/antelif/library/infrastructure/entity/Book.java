package com.antelif.library.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Book {
  private String isbn;
  private String title;
  private Author author;
  private Publisher publisher;
}
