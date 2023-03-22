package com.antelif.library.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Book request DTO used as request body in HTTP requests.
 */
@Getter
@Setter
@ToString
public class BookRequest {

  @NotBlank(message = "Book title should not be blank.")
  @Size(max = 50, message = "Book title cannot exceed 50 characters.")
  private String title;

  @NotBlank(message = "Book ISBN cannot be blank.")
//  @ISBN(message = "Book ISBN should have correct format.", type = Type.ANY)
  private String isbn;

  @NotNull(message = "Author id cannot be blank.")
  private Long authorId;

  @NotNull(message = "Publisher id cannot be blank")
  private Long publisherId;
}
