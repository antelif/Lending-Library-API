package com.antelif.library.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author Request DTO used as request body in HTTP requests.
 */
@Getter
@Setter
@ToString
public class AuthorRequest {

  @NotBlank(message = "Author name should not be blank.")
  @Size(max = 50, message = "Author name should not exceed 50 characters.")
  private String name;

  @Size(max = 50, message = "Author middle name should not exceed 50 characters.")
  private String middleName;

  @NotBlank(message = "Author last name should not be blank.")
  @Size(max = 50, message = "Author last name should not exceed 50 characters.")
  private String surname;
}
