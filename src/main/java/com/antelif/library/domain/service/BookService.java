package com.antelif.library.domain.service;

import com.antelif.library.domain.converter.BookConverter;
import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** BookEntity Service. */
@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final BookConverter bookConverter;

  public BookDto getBookByIsbn(String isbn) {
    return bookConverter.convertFromDomainToDto(
        bookConverter.convertFromEntityToDomain(bookRepository.getBookByIsbn(isbn)));
  }

  /**
   * Adds a new BookEntity entity objet to database.
   *
   * @param bookDto the DTO to get information about the new book to add.
   * @return the title of the book added.
   */
  public String addBook(BookDto bookDto) {
    return bookRepository
        .save(
            bookConverter.convertFromDomainToEntity(bookConverter.convertFromDtoToDomain(bookDto)))
        .getTitle();
  }
}
