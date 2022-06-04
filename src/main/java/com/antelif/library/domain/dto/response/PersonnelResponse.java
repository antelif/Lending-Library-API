package com.antelif.library.domain.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Personnel response DTO used as response body in HTTP requests. */
@Getter
@Setter
@EqualsAndHashCode
public class PersonnelResponse {

  private Long id;
  private String username;
}
