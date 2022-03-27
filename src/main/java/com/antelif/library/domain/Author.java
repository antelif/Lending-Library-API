package com.antelif.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Author domain object. */
@Getter
@Setter
@EqualsAndHashCode
public class Author {
  private String name;
  private String middleName;
  private String surname;
}
