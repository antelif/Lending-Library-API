package com.antelif.library.domain.service;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Book copy service. */
@Service
@RequiredArgsConstructor
public class BookCopyService {

  /**
   * Retuof the copies added.rn the number of copies added.
   *
   * @param bookCopies the copies to add in the database.
   * @return the number
   */
  public Integer addBookCopies(List<BookCopyRequest> bookCopies) {

    return 0;
  }
}
