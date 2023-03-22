package com.antelif.library.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Author response DTO used as response body in HTTP requests.
 */
@Getter
@Setter
public class AuthorResponse {

  private Long id;
  private String name;
  private String surname;
  private String middleName;
}
