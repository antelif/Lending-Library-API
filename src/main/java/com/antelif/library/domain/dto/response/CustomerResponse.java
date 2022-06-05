package com.antelif.library.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Customer response DTO used as response body in HTTP requests. */
@Getter
@Setter
public class CustomerResponse {
  private Long id;
  private String name;
  private String surname;
  private String phoneNo;
  private String email;
  private double fee;
}
