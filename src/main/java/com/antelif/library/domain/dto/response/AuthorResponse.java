package com.antelif.library.domain.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Author response DTO used as response body in HTTP requests. */
@Getter
@Setter
@EqualsAndHashCode
public class AuthorResponse {

  private String id;
  private String name;
  private String surname;
  private String middleName;
}
