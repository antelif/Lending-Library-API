package com.antelif.library.infrastructure.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Publisher {

  private String name;
  private List<Book> books;

}
