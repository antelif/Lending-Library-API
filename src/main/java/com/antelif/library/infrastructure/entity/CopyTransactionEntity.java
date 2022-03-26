package com.antelif.library.infrastructure.entity;

import com.antelif.library.infrastructure.ids.CopyTransactionId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** CopyTransactionEntity entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "copy_transaction")
@IdClass(value = CopyTransactionId.class)
// TODO: revise name.
public class CopyTransactionEntity {

  @Id
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private BookCopyEntity bookCopy;

  @Id
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private TransactionEntity transaction;
}
