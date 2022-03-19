package com.antelif.library.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String isbn;

  private String title;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Author author;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Publisher publisher;
}
