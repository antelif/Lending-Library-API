package com.antelif.library.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Author Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class AuthorRequest {

  private String name;
  private String middleName;
  private String surname;
}
