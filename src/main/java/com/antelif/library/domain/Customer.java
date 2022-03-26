package com.antelif.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Customer domain object. */
@Getter
@Setter
@EqualsAndHashCode
public class Customer {

  private String name;
  private String surname;
  private String phoneNo;
  private String email;
  private double fee;

  /**
   * Checks if customer need to pay fee before borrowing books again
   *
   * @return true if there is no fee pending, false otherwise.
   */
  public boolean canBorrow() {
    return this.fee <= 0;
  }
}
