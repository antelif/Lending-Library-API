package com.antelif.library.domain.dto;

import com.antelif.library.infrastructure.entity.State;
import com.antelif.library.infrastructure.entity.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** BookCopy DTO. */
@Getter
@Setter
@EqualsAndHashCode
public class BookCopyDto {
  private String title;
  private State state;
  private Status status;
}
