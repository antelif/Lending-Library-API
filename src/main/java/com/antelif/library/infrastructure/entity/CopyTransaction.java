package com.antelif.library.infrastructure.entity;

import com.antelif.library.infrastructure.ids.CopyTransactionId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(value = CopyTransactionId.class)
public class CopyTransaction {

  @Id
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private BookCopy bookCopy;

  @Id
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Transaction transaction;
}
