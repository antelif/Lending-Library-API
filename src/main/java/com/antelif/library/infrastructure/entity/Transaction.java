package com.antelif.library.infrastructure.entity;

import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/** Transaction entity that gets persisted in database. */
@Getter
@Setter
@Entity
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Instant creationDate;

  private Instant returnDate;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Customer customer;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(referencedColumnName = "id")
  private Personnel personnel;
}
