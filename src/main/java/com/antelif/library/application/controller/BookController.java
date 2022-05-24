package com.antelif.library.application.controller;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.service.BookCopyService;
import com.antelif.library.domain.service.BookService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Book Controller. */
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
  public BookResponse getBookByIsbn(@PathVariable("isbn") String isbn) {

    log.info("Request to get book with isbn: {}", isbn);

    return bookService.getBookByIsbn(isbn);
  }

  /**
   * Add a new book in the database.
   *
   * @param dto the DTo with information about the new book.
   * @return a map with the book title as value.
   */
  @PostMapping
  public Map<String, String> addBook(@RequestBody BookRequest dto) {
    log.info("Request to add book with id: {}", dto.getIsbn());
    return Map.of("created", bookService.addBook(dto));
  }

  /**
   * Add book copies endpoint.
   *
   * @param isbn o the book, to add copies for.
   * @param copies the BookCopy DTOs with information about each copy.
   * @return a map.
   */
  @PostMapping(value = "/copies/{isbn}")
  public Map<String, String> addMultipleBookCopies(
      @PathVariable(value = "isbn") String isbn, @RequestBody List<BookCopyRequest> copies) {
    log.info("Request to add {} book copies for with id: {}", copies.size(), isbn);
    return Map.of("created", bookCopyService.addBookCopies(copies).toString());
  }
}
