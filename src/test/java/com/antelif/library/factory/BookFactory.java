package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;

public class BookFactory {

  public static BookRequest createBookRequest(int index, long authorId) {
    var book = new BookRequest();
    book.setIsbn(index + "-" + index + "-");
    book.setTitle("title" + index);
    book.setAuthorId(index);
    //    book.setPublisherId(index);
    return book;
  }

  public static BookResponse createBookResponse(int index, long authorId) {
    var book = new BookResponse();
    book.setIsbn(index + "-" + index + "-");
    book.setTitle("title" + index);
    book.setAuthorId(String.valueOf(index));
    //    book.setPublisherId(index);
    return book;
  }
}
