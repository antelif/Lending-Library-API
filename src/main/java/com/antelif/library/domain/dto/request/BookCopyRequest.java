package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;

import com.antelif.library.domain.type.BookCopyStatus;
import com.antelif.library.domain.type.State;
import javax.validation.constraints.NotBlank;
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
  @ISBN(message = "Provide a valid ISBN.")
  private String isbn;

  @NotBlank(message = "Book copy state cannot be blank.")
  private State state;

  private BookCopyStatus status = AVAILABLE;
}
