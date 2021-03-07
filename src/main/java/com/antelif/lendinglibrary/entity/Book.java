package com.antelif.lendinglibrary.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Book {

  @Id
  private String isbn;
  private String title;
  private String author;
  private String publisher;

  @Override
  public String toString() {
    return title + ", by " + author + "\nISBN: " + isbn;
  }


}
