package com.antelif.library.infrastructure.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Customer entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String surname;

  private String phoneNo;

  private String email;

  private double fee = 0;

  @OneToMany(mappedBy = "customer")
  private Set<TransactionEntity> transactions;
}
