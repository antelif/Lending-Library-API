package com.antelif.library.infrastructure.ids;

import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.entity.TransactionEntity;
import java.io.Serializable;
import lombok.Getter;

/**
 * Contains copy transaction table primary key.
 */
@Getter
public class TransactionItemId implements Serializable {

  private BookCopyEntity bookCopy;
  private TransactionEntity transaction;
}
