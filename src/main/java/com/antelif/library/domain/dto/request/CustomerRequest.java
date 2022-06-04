package com.antelif.library.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Customer Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class CustomerRequest {

  private String name;
  private String surname;
  private String phoneNo;
  private String email;
  private double fee = 0;
}
