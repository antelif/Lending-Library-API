package com.antelif.library.infrastructure.entity;

import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;
import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;
import static com.antelif.library.domain.type.TransactionStatus.CANCELLED;
import static com.antelif.library.domain.type.TransactionStatus.FINALIZED;
import static java.time.temporal.ChronoUnit.DAYS;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

import com.antelif.library.domain.type.TransactionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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

  @Enumerated(STRING)
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

  /**
   * Calculates and sets return date.
   *
   * @param daysUntilReturn the days to return the book as provided during a transaction.
   */
  public void setDates(int daysUntilReturn) {
    this.creationDate = Instant.now();
    this.returnDate = calculateReturnDate(daysUntilReturn);
  }

  /**
   * Creates a list of TransactionItemEntity for each one of the book copies provided.
   *
   * @param bookCopies the list with the BookCopyEntity objects of the transaction to create
   *     TransactionEntityItems for.
   * @return a set of TransactionItemEntity objects, one for each book copy.
   */
  public Set<TransactionItemEntity> createTransactionItemsOfCopies(
      List<BookCopyEntity> bookCopies) {
    return bookCopies.stream()
        .map(
            bookCopy -> {
              var transactionItem = new TransactionItemEntity();
              transactionItem.setBookCopy(bookCopy);
              return transactionItem;
            })
        .collect(Collectors.toSet());
  }

  private Instant calculateReturnDate(int daysUntilReturn) {
    return Instant.now().plus(daysUntilReturn, DAYS).truncatedTo(DAYS);
  }

  /**
   * Toggles book copy entity item statuses of this transaction.
   *
   * @param bookCopyIds the ids of the book copies to toggle status.
   */
  public void updateTransactionItems(List<Long> bookCopyIds) {
    transactionItems.stream()
        .map(TransactionItemEntity::getBookCopy)
        .filter(bookCopy -> bookCopyIds.contains(bookCopy.getId()))
        .forEach(BookCopyEntity::toggleStatus);
  }

  /**
   * A transaction can be finalized if all book copies of this transaction are returned, aka
   * AVAILABLE.
   *
   * @return true if a transaction is finalized.
   */
  public boolean canBeFinalized() {
    return this.transactionItems.stream()
        .map(TransactionItemEntity::getBookCopy)
        .map(BookCopyEntity::getStatus)
        .allMatch(status -> status.equals(AVAILABLE));
  }

  /** Sets the transaction status to 'FINALIZED' */
  public void finalizeTransaction() {
    this.status = FINALIZED;
  }

  /**
   * Cancels a transaction by setting the transaction status to 'CANCELLED' and toggling each status
   * of book copies in transaction back to AVAILABLE.
   */
  public void cancelTransaction() {
    this.status = CANCELLED;
    this.transactionItems.stream()
        .map(TransactionItemEntity::getBookCopy)
        .forEach(BookCopyEntity::toggleStatus);
  }
}
