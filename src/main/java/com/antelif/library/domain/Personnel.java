package com.antelif.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Personnel domain object. */
@Getter
@Setter
@EqualsAndHashCode
public class Personnel {
  private String username;
  private String password;
}
