package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;

import com.antelif.library.domain.type.BookCopyStatus;
import com.antelif.library.domain.type.State;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.ISBN;

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

  private BookCopyStatus status = AVAILABLE;
}
