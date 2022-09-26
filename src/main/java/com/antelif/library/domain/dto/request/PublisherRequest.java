package com.antelif.library.domain.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Publisher Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class PublisherRequest {

  @NotBlank(message = "Publisher name should not be blank.")
  private String name;
}
