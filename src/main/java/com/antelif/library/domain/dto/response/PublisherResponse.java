package com.antelif.library.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Publisher Request DTO used as response body in HTTP requests.
 */
@Getter
@Setter
public class PublisherResponse {

  private Long id;
  private String name;

}
