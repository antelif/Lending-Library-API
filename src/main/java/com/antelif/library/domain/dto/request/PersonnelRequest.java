package com.antelif.library.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Personnel Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class PersonnelRequest {

  @NotBlank(message = "Personnel username should not be blank.")
  @Size(max = 20, message = "Personnel username should not exceed 20 characters.")
  private String username;

  @NotBlank(message = "Personnel password should not be blank.")
  private String password;
}
