package com.antelif.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Customer {

  private String name;
  private String surname;
  private String phoneNo;
  private String email;
  private double fee;

  public boolean canBorrow() {
    return this.fee <= 0;
  }
}
