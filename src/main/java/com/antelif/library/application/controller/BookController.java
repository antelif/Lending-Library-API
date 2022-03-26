package com.antelif.library.application.controller;

import com.antelif.library.domain.dto.BookCopyDto;
import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.domain.service.BookCopyService;
import com.antelif.library.domain.service.BookService;
import com.antelif.library.infrastructure.entity.BookEntity;
import java.util.List;
import java.util.Map;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** BookEntity Controller. */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

  private final BookService bookService;
  private final BookCopyService bookCopyService;

  /**
   * Gets a book by Isbn.
   *
   * @param isbn of the book requested.
   * @return a book having the given isbn.
   */
  @GetMapping(value = "/{isbn}")
  public BookEntity getBookByIsbn(@PathVariable("isbn") String isbn) {

    log.info("Request to get BookEntity with isbn: {}", isbn);

    return bookService.getBookByIsbn(isbn);
  }

  /**
   * Add a new book in the database.
   * @param dto the DTo with information about the new book.
   * @return a map with the book title as value.
   */
  @PostMapping
  public Map<String, String> addBook(@RequestBody BookDto dto) {
    log.info("Request to add book with id: {}", dto.getIsbn());
    return Map.of("created", bookService.addBook(dto));
  }

  @PostMapping(value = "/copies/{isbn}")
  public Map<String, String> addMultipleBookCopies(@PathParam(value = "isbn")String isbn, @RequestBody List<BookCopyDto> copies){
    log.info("Request to add {} book copies for with id: {}", isbn);
    return Map.of("created", bookCopyService.addBookCopies(copies).toString());
  }
//
//  @GetMapping(value = "/books", params = "author")
//  public List<BookEntity> getBookByAuthor(@RequestParam("author") String author) {
//    log.info("Request to get all Books by athor: {}", author);
//    return bookService.getBookByAuthor(author);
//  }
//
//  @GetMapping(value = "/books", params = "title")
//  public List<BookEntity> getBooksByTitle(@RequestParam("title") String title) {
//
//    log.info("Request to get all Books with keyword: {}", title);
//
//    return bookService.getBooksByTitle(title);
//  }
//
//  @GetMapping(value = "/books", params = "publisher")
//  public List<BookEntity> getBooksByPublisher(@PathParam("publisher") String publisher) {
//    return bookService.getBooksByPublisher(publisher);
//  }
//
//  @PutMapping("books/{isbn}")
//  public void updateBook(BookEntity book) {
//    log.info("Request to update boom with isbn: {}", book.getIsbn());
//    bookService.updateBook(book);
//  }
//
//  @DeleteMapping("books/{isbn}")
//  public void removeBook(String isbn) {
//    log.info("Request to delete book with isbn: {}", isbn);
//    bookService.removeBook(isbn);
//  }
}
