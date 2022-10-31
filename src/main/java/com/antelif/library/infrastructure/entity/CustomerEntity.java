package com.antelif.library.infrastructure.entity;

import static com.antelif.library.domain.common.InstantConversions.nowInstantToDays;
import static com.antelif.library.domain.type.TransactionStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.DAYS;
import static javax.persistence.FetchType.EAGER;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
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

  private Instant lastUpdate = nowInstantToDays();

  @OneToMany(mappedBy = "customer", fetch = EAGER)
  private Set<TransactionEntity> transactions;

  /**
   * Repay a fee amount by subtracting it.
   *
   * @param fee the amount to subtract.
   */
  public void payFee(double fee) {
    this.fee -= fee;
  }

  /**
   * Update the customer fee if fee is not updated the last day.
   *
   * @param dailyRate the daily fee to add per late book.
   */
  public void updateFee(double dailyRate) {

    if (feeIsUpdateable()) {
      calculateFee(dailyRate);
      updateLastUpdateTime();
    }
  }

  /**
   * Calculates the customer fee using the fee rate provided. Each day is multiplied by the fee rate
   * for all unreturned books in active transactions that are due their return date.
   *
   * @param dailyRate the rate to use to calculate fee.
   */
  private void calculateFee(double dailyRate) {

    var now = nowInstantToDays();
    this.fee = 0;

    Optional.ofNullable(transactions).stream()
        .flatMap(Collection::stream)
        .filter(transaction -> ACTIVE.equals(transaction.getStatus()))
        .forEach(
            transaction -> {
              if (now.isAfter(transaction.getReturnDate())) {
                var daysInBetween = DAYS.between(transaction.getReturnDate(), now);
                fee += dailyRate * daysInBetween;
                lastUpdate = nowInstantToDays();
              }
            });
  }

  /** Updates the lat update time. Truncates to days. */
  private void updateLastUpdateTime() {
    this.lastUpdate = nowInstantToDays();
  }

  /** Returns true if the fee was not updated the last day. */
  private boolean feeIsUpdateable() {
    return nowInstantToDays().isAfter(lastUpdate);
  }
}
