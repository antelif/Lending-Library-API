package com.antelif.library.domain.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Publisher Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@EqualsAndHashCode
public class PublisherRequest {

  private String name;
}
