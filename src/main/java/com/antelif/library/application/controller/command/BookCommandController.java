package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.service.BookService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Book command controller. */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/library/books")
public class BookCommandController {

  private final BookService bookService;

  /**
   * Add a new book endpoint.
   *
   * @param bookRequest the DTO to get information to create the new book.
   * @return a book response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, BookResponse>> addBook(@RequestBody BookRequest bookRequest) {
    log.info("Received request to add book {}", bookRequest);
    return ResponseEntity.ok(Map.of(CREATED, bookService.addBook(bookRequest)));
  }
}
