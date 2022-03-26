package com.antelif.library.domain.service;

import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.domain.factory.ConverterFactory;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** BookEntity Service. */
@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final ConverterFactory converterFactory;

  public BookEntity getBookByIsbn(String isbn) {
    return bookRepository.getBookByIsbn(isbn);
  }

//  public List<BookEntity> getBookByAuthor(String author) {
//    return bookRepository.getBooksByAuthorName(author);
//  }
//
//  public List<BookEntity> getBooksByTitle(String title) {
//    return bookRepository.getBooksByTitleContaining(title);
//  }
//
//  public List<BookEntity> getBooksByPublisher(String publisher) {
//    return bookRepository.getBooksByPublisherName(publisher);
//  }

  /**
   * Adds a new BookEntity entity objet to database.
   *
   * @param bookDto the DTO to get information about the new book to add.
   * @return the title of the book added.
   */
  public String addBook(BookDto bookDto) {
    return bookRepository
        .save(
            (BookEntity)
                (converterFactory
                    .getConverter(bookDto.getClass().toString())
                    // TODO: Replace with customized
                    .map(c -> c.convertFromDtoToDomain(bookDto))
                    .orElseThrow(RuntimeException::new)))
        .getTitle();
  }

  public void removeBook(String isbn) {
    bookRepository.deleteBookByIsbn(isbn);
  }

  public void updateBook(BookEntity book) {
    bookRepository.save(book);
  }
}
