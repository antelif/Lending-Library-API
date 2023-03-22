package com.antelif.library.domain.type;

import lombok.Getter;

/** State of the book copy. */
@Getter
public enum State {
  NEW(1),
  GOOD(2),
  BAD(3);

  private final int stateOrder;

  State(int stateOrder) {
    this.stateOrder = stateOrder;
  }
}
