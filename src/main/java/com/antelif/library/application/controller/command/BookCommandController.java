package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.ControllerTags.BOOK_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.BOOKS_ENDPOINT;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.service.BookService;
import io.swagger.annotations.Api;
import java.util.Map;
import javax.validation.Valid;
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
@Api(tags = BOOK_CONTROLLER)
@RequestMapping(value = BOOKS_ENDPOINT)
public class BookCommandController {

  private final BookService bookService;

  /**
   * Add a new book endpoint.
   *
   * @param bookRequest the DTO to get information to create the new book.
   * @return a book response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, BookResponse>> addBook(
      @RequestBody @Valid BookRequest bookRequest) {
    log.info("Received request to add book {}", bookRequest);
    return ResponseEntity.ok(Map.of(CREATED, bookService.addBook(bookRequest)));
  }
}
