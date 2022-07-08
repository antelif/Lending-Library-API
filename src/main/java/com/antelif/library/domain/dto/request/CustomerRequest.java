package com.antelif.library.domain.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Customer Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class CustomerRequest {

  @NotBlank(message = "Customer name should not be blank.")
  @Size(max = 50, message = "Customer name should not exceed 50 characters.")
  private String name;

  @NotBlank(message = "Customer surname should not be blank.")
  @Size(max = 50, message = "Customer surname should not exceed 50 characters.")
  private String surname;

  @Pattern(regexp = "\\d+", message = "Customer phone number should contain digits.")
  private String phoneNo;

  @Email(message = "Customer should contain a valid email.")
  private String email;

  private double fee = 0;
}
