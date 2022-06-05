package com.antelif.library.factory;

import static com.antelif.library.factory.AuthorFactory.createAuthorResponse;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;

public class BookFactory {

  public static BookRequest createBookRequest(int index, long authorId, long publisherId) {
    var book = new BookRequest();
    book.setIsbn("isbn" + index);
    book.setTitle("title" + index);
    book.setAuthorId(authorId);
    book.setPublisherId(publisherId);
    return book;
  }

  public static BookResponse createBookResponse(
      int authorIndex, int publisherIndex, int bookIndex) {
    var book = new BookResponse();
    book.setIsbn("isbn" + bookIndex);
    book.setTitle("title" + bookIndex);
    book.setAuthor(createAuthorResponse(authorIndex));
    book.setPublisher(PublisherFactory.createPublisherResponse(publisherIndex));
    return book;
  }
}
