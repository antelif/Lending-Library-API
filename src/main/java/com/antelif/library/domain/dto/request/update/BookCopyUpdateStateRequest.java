package com.antelif.library.domain.dto.request.update;

import com.antelif.library.domain.type.State;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Book copy state request DTO used as request body in HTTP requests to update the book copy state.
 */
@Getter
@Setter
public class BookCopyUpdateStateRequest {

  @NotNull(message = "Book copy state should not be blank.")
  private State state;
}
