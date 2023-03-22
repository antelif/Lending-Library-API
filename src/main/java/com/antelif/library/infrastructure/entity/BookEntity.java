package com.antelif.library.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * BookEntity entity object that gets persisted in database.
 */
@Setter
@Getter
@Entity
@Table(name = "book")
public class BookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String isbn;

  private String title;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private AuthorEntity author;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private PublisherEntity publisher;
}
