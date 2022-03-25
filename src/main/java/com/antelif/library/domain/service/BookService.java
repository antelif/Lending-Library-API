package com.antelif.library.domain.service;

import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.domain.factory.ConverterFactory;
import com.antelif.library.infrastructure.entity.Book;
import com.antelif.library.infrastructure.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Book Service. */
@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final ConverterFactory converterFactory;

  public Book getBookByIsbn(String isbn) {
    return bookRepository.getBookByIsbn(isbn);
  }

  public List<Book> getBookByAuthor(String author) {
    return bookRepository.getBooksByAuthorName(author);
  }

  public List<Book> getBooksByTitle(String title) {
    return bookRepository.getBooksByTitleContaining(title);
  }

  public List<Book> getBooksByPublisher(String publisher) {
    return bookRepository.getBooksByPublisherName(publisher);
  }

  /**
   * Adds a new Book entity objet to database.
   *
   * @param bookDto the DTO to get information about the new book to add.
   * @return the title of the book added.
   */
  public String addBook(BookDto bookDto) {
    return bookRepository
        .save(
            (Book)
                (converterFactory
                    .getConverter(bookDto.getClass().toString())
                    // TODO: Replace with customized
                    .map(c -> c.convertToDomain(bookDto))
                    .orElseThrow(RuntimeException::new)))
        .getTitle();
  }

  public void removeBook(String isbn) {
    bookRepository.deleteBookByIsbn(isbn);
  }

  public void updateBook(Book book) {
    bookRepository.save(book);
  }
}
