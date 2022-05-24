package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.dto.response.PublisherResponse;

public class BookFactory {

  public static BookRequest createBookRequest(int index, Long authorId, Long publisherId) {
    var book = new BookRequest();
    book.setIsbn(index + "-" + index + "-");
    book.setTitle("title" + index);
    book.setAuthorId(authorId);
    book.setPublisherId(publisherId);
    return book;
  }

  public static BookResponse createBookResponse(
      int index, AuthorResponse author, PublisherResponse publisher) {
    var book = new BookResponse();
    book.setIsbn(index + "-" + index + "-");
    book.setTitle("title" + index);
    book.setAuthor(author);
    book.setPublisher(publisher);
    return book;
  }
}
