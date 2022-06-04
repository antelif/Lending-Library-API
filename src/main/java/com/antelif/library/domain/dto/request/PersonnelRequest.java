package com.antelif.library.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Personnel Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class PersonnelRequest {

  private String username;
  private String password;
}
