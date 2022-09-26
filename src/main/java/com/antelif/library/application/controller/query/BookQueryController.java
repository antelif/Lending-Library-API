package com.antelif.library.application.controller.query;

import static com.antelif.library.domain.common.ControllerTags.BOOK_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;

import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.service.BookService;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Book query controller */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = BOOK_CONTROLLER)
@RequestMapping(value = BOOKS_ENDPOINT)
public class BookQueryController {





  private final BookService bookService;

  @GetMapping
  public ResponseEntity<List<BookResponse>> getBooks() {
    log.info("Received request to get all books.");

    return ResponseEntity.ok(bookService.getAllBooks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id) {
    log.info("received request to get book with id: {} ", id);
    return ResponseEntity.ok(bookService.getBookById(id));
  }
}
