package com.antelif.library.infrastructure.entity;

import com.antelif.library.infrastructure.ids.TransactionItemId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * CopyTransactionEntity entity object that gets persisted in database.
 */
@Getter
@Setter
@Entity
@Table(name = "transaction_item")
@IdClass(value = TransactionItemId.class)
public class TransactionItemEntity {

  @Id
  @ManyToOne
  @JoinColumn(name = "book_copy_id")
  private BookCopyEntity bookCopy;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id")
  private TransactionEntity transaction;
}
