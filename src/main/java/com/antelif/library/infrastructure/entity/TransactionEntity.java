package com.antelif.library.infrastructure.entity;

import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** TransactionEntity entity that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class TransactionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Instant creationDate;

  private Instant returnDate;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private CustomerEntity customer;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(referencedColumnName = "id")
  private PersonnelEntity personnel;
}
