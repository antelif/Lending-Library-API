package com.antelif.library.infrastructure.entity;

import com.antelif.library.domain.type.State;
import com.antelif.library.domain.type.Status;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** BookCopyEntity entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "book_copy")
public class BookCopyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private BookEntity book;

  @Enumerated(value = EnumType.STRING)
  private State state;

  @Enumerated(value = EnumType.STRING)
  private Status status;
}
