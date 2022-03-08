package com.antelif.library.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LendingStatus {

  private Transaction latestTransaction;
  private Status status;

}
