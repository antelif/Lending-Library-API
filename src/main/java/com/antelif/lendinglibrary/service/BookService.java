package com.antelif.lendinglibrary.service;

import com.antelif.lendinglibrary.entity.Book;
import com.antelif.lendinglibrary.repository.BookRepository;
import java.security.PublicKey;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public Book getBookByIsbn(String isbn) {
    return bookRepository.getBookByIsbn(isbn);
  }

  public List<Book> getBookByAuthor(String author) {
    List<Book> books = bookRepository.getBooksByAuthor(author);
    return books;
  }

  public List<Book> getBooksByTitle(String title) {
    List<Book> books = bookRepository.getBooksByTitleContaining(title);
    return books;
  }

  public List<Book> getBooksByPublisher(String publisher){
    List<Book> books = bookRepository.getBooksByPublisher(publisher);
    return  books;
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
