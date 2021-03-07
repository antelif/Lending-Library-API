package com.antelif.lendinglibrary.controller;

import com.antelif.lendinglibrary.entity.Book;
import com.antelif.lendinglibrary.service.BookService;
import java.util.List;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Book Controller.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {

  @Autowired
  private BookService bookService;

  /**
   * Gets a book by Isbn.
   *
   * @param isbn of the book requested.
   * @return a book having the given isbn.
   */
  @GetMapping("/books/{isbn}")
  public Book getBookByIsbn(@PathVariable("isbn") String isbn) {
    Book book;

    log.info("Request to get Book with isbn: {}", isbn);

    book = bookService.getBookByIsbn(isbn);

    return book;
  }

  @GetMapping(value = "/books", params = "author")
  public List<Book> getBookByAuthor(@RequestParam("author") String author) {
    List<Book> books = bookService.getBookByAuthor(author);
    log.info("Request to get all Books by athor: {}", author);
    return books;
  }

  @GetMapping(value = "/books", params = "title")
  public List<Book> getBooksByTitle(@RequestParam("title") String title) {
    List<Book> books = bookService.getBooksByTitle(title);
    log.info("Request to get all Books with keyword: {}", title);
    return books;
  }

  @GetMapping(value = "/books", params = "publisher")
  public List<Book> getBooksByPublisher(@PathParam("publisher") String publisher){
    return bookService.getBooksByPublisher(publisher);
  }

  @PostMapping("/books/{isbn}")
  public void addBook(@RequestBody Book book) {
    log.info("Request to add book with id: {}", book.getIsbn());
    bookService.addBook(book);
  }

  @PutMapping("books/{isbn}")
  public void updateBook(Book book) {
    log.info("Request to update boom with isbn: {}",book.getIsbn());
    bookService.updateBook(book);
  }

  @DeleteMapping("books/{isbn}")
  public void removeBook(String isbn) {
    log.info("Request to delete book with isbn: {}",isbn);
    bookService.removeBook(isbn);
  }
}
