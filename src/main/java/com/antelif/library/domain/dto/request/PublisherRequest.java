package com.antelif.library.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Publisher Request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class PublisherRequest {

  private String name;
}
