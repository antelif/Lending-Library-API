package com.antelif.library.infrastructure.entity;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author {

  private String name;
  private String surname;
  private List<Book> books;
}
