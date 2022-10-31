package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.ControllerTags.BOOK_COPY_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.BOOK_COPIES_ENDPOINT;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.service.BookCopyService;
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

/** Book copies command controller. */
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = BOOK_COPY_CONTROLLER)
@RequestMapping(value = BOOK_COPIES_ENDPOINT)
public class BookCopiesCommandController {

  private final BookCopyService bookCopyService;

  /**
   * Add new book copy endpoint.
   *
   * @param bookCopyRequest the DTO to get information to create the new book copy.
   * @return a book copy response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, BookCopyResponse>> addBookCopy(
      @RequestBody @Valid BookCopyRequest bookCopyRequest) {
    log.info("Received request to add book copy {}", bookCopyRequest);
    return ResponseEntity.ok(Map.of(CREATED, bookCopyService.addBookCopy(bookCopyRequest)));
  }
}
