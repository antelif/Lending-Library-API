package com.antelif.library.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** AuthorEntity DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {

  private Long id;
  private String name;
  private String surname;
}
