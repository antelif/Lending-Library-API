package com.antelif.library.infrastructure.ids;

import com.antelif.library.infrastructure.entity.BookCopy;
import com.antelif.library.infrastructure.entity.Transaction;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CopyTransactionId implements Serializable {

  private BookCopy bookCopy;
  private Transaction transaction;
}
