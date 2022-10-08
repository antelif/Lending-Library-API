package com.antelif.library.factory;

import static com.antelif.library.domain.type.BookCopyStatus.AVAILABLE;
import static com.antelif.library.domain.type.BookCopyStatus.LENT;
import static com.antelif.library.domain.type.State.NEW;
import static com.antelif.library.factory.BookFactory.createBookResponse;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;

public class BookCopyFactory {

  public static BookCopyRequest createBookCopyRequest(String isbn) {
    var bookCopy = new BookCopyRequest();
    bookCopy.setIsbn(isbn);
    bookCopy.setState(NEW);
    return bookCopy;
  }

  public static BookCopyResponse createBookCopyResponse(
      int authorIndex, int publisherIndex, int bookIndex) {
    var bookCopy = new BookCopyResponse();

    bookCopy.setState(NEW);
    bookCopy.setStatus(AVAILABLE);

    bookCopy.setBook(createBookResponse(authorIndex, publisherIndex, bookIndex));

    return bookCopy;
  }

  public static BookCopyResponse createBookCopyResponseAfterTransaction(
      int authorIndex, int publisherIndex, int bookIndex) {
    var bookCopy = createBookCopyResponse(authorIndex, publisherIndex, bookIndex);
    bookCopy.setStatus(LENT);

    return bookCopy;
  }
}
