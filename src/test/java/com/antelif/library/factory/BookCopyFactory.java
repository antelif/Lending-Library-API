package com.antelif.library.factory;

import static com.antelif.library.domain.type.State.EXCELLENT;
import static com.antelif.library.domain.type.Status.SHELF;
import static com.antelif.library.factory.BookFactory.createBookResponse;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;

public class BookCopyFactory {

  public static BookCopyRequest createBookCopyRequest(String isbn) {
    var bookCopy = new BookCopyRequest();
    bookCopy.setIsbn(isbn);
    bookCopy.setState(EXCELLENT);
    return bookCopy;
  }

  public static BookCopyResponse createBookCopyResponse(
      int authorIndex, int publisherIndex, int bookIndex) {
    var bookCopy = new BookCopyResponse();

    bookCopy.setState(EXCELLENT);
    bookCopy.setStatus(SHELF);

    bookCopy.setBook(createBookResponse(authorIndex, publisherIndex, bookIndex));

    return bookCopy;
  }
}
