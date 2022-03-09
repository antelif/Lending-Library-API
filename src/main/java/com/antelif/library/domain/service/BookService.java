package com.antelif.library.domain.service;

import com.antelif.library.infrastructure.entity.Book;
import com.antelif.library.infrastructure.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;

  public Book getBookByIsbn(String isbn) {
    return bookRepository.getBookByIsbn(isbn);
  }

  public List<Book> getBookByAuthor(String author) {
    return bookRepository.getBooksByAuthor(author);
  }

  public List<Book> getBooksByTitle(String title) {
    return bookRepository.getBooksByTitleContaining(title);
  }

  public List<Book> getBooksByPublisher(String publisher) {
    return bookRepository.getBooksByPublisher(publisher);
  }

  public void addBook(Book book) {
    bookRepository.save(book);
  }

  public void removeBook(String isbn) {
    bookRepository.deleteBookByIsbn(isbn);
  }

  public void updateBook(Book book) {
    bookRepository.save(book);
  }

}
