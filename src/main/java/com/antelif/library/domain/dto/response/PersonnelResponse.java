package com.antelif.library.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Personnel response DTO used as response body in HTTP requests. */
@Getter
@Setter
public class PersonnelResponse {

  private Long id;
  private String username;
  private String role;
}
