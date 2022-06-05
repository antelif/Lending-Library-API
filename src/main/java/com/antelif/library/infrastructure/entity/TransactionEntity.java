package com.antelif.library.infrastructure.entity;

import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.DAYS;
import static javax.persistence.CascadeType.ALL;

import com.antelif.library.domain.type.BookCopyStatus;
import com.antelif.library.domain.type.TransactionStatus;
import java.time.Instant;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

  private TransactionStatus status = ACTIVE;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private CustomerEntity customer;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private PersonnelEntity personnel;

  @OneToMany(mappedBy = "transaction", cascade = ALL)
  private Set<TransactionItemEntity> transactionItems;

  /**
   * Set transactionItems set to a given set of TransactionItemEntity objects.
   *
   * @param transactionItems the set of items to replace the existing items set of this entity.
   */
  public void addItems(Set<TransactionItemEntity> transactionItems) {
    transactionItems.forEach(i -> i.setTransaction(this));
    this.transactionItems = Set.copyOf(transactionItems);
  }

  private Instant calculateReturnDate(int daysUntilReturn) {
    return Instant.now().plus(daysUntilReturn, DAYS).truncatedTo(DAYS);
  }

  public void setDates(int daysUntilReturn){
    this.creationDate = Instant.now();
    this.returnDate = calculateReturnDate(daysUntilReturn);
  }
}
