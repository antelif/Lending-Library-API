package com.antelif.library.domain.dto.request;

import com.antelif.library.domain.type.State;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Book copy request DTO used as request body in HTTP requests. */
@Getter
@Setter
@ToString
public class BookCopyRequest {

  @NotBlank(message = "Book copy ISBN cannot be blank.")
  //  @ISBN(message = "Book ISBN should have correct format.")
  private String isbn;

  @NotNull(message = "Book copy state cannot be blank.")
  private State state;
}
