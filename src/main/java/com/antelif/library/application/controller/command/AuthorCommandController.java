package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;
import static com.antelif.library.domain.common.ControllerTags.AUTHOR_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.AUTHORS_ENDPOINT;

import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.service.AuthorService;
import io.swagger.annotations.Api;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Author command controller. */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = AUTHOR_CONTROLLER)
@RequestMapping(value = AUTHORS_ENDPOINT)
public class AuthorCommandController {

  private final AuthorService authorService;

  /**
   * Add a new author endpoint.
   *
   * @param authorRequest the DTO to get information to create the new author.
   * @return an author response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, AuthorResponse>> addAuthor(
      @RequestBody AuthorRequest authorRequest) {
    log.info("Received request to add author {}", authorRequest);
    return ResponseEntity.ok(Map.of(CREATED, authorService.addAuthor(authorRequest)));
  }
}
