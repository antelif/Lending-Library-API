package com.antelif.library.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Author DTO. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AuthorDto {

  private String name;
  private String middleName;
  private String surname;
}
