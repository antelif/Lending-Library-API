package com.antelif.library.infrastructure.ids;

import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Contains copy transaction table primary key. */
@Getter
@Setter
@EqualsAndHashCode
public class CopyTransactionId implements Serializable {

  private BookCopyEntity bookCopy;
  private TransactionEntity transaction;
}
