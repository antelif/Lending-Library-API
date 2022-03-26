package com.antelif.library.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Author DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {

  private String name;
  private String surname;
}
