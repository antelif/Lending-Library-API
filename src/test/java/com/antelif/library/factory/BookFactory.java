package com.antelif.library.factory;

import static com.antelif.library.factory.AuthorFactory.createAuthorResponse;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import lombok.SneakyThrows;

public class BookFactory {

  public static BookRequest createBookRequest(int bookIndex, long authorId, long publisherId) {
    BookRequest book = new BookRequest();
    book.setIsbn("isbn" + bookIndex);
    book.setTitle("title" + bookIndex);
    book.setAuthorId(authorId);
    book.setPublisherId(publisherId);
    return book;
  }

  @SneakyThrows
  public static BookResponse createBookResponse(int authorId, int publisherId, int bookIndex) {
    BookResponse book = new BookResponse();
    book.setIsbn("isbn" + bookIndex);
    book.setTitle("title" + bookIndex);
    book.setAuthor(createAuthorResponse(authorId));
    book.setPublisher(PublisherFactory.createPublisherResponse(publisherId));
    return book;
  }
}
