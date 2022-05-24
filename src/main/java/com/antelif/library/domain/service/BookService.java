package com.antelif.library.domain.service;

import com.antelif.library.domain.converter.BookConverter;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** BookEntity Service. */
@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final BookConverter bookConverter;

  public BookResponse getBookByIsbn(String isbn) {
    return bookConverter.convertFromEntityToResponse(bookRepository.getBookByIsbn(isbn));
  }

  /**
   * Adds a new BookEntity entity objet to database.
   *
   * @param bookRequest the DTO to get information about the new book to add.
   * @return the title of the book added.
   */
  public String addBook(BookRequest bookRequest) {
    return bookRepository.save(bookConverter.convertFromRequestToEntity(bookRequest)).getTitle();
  }
}
